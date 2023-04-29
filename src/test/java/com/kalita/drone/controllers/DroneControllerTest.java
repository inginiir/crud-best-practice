package com.kalita.drone.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.entities.Model;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
class DroneControllerTest {

    private final static String URL_API_DRONE = "/api/drone";
    private final MockMvc mockMvc;

    @Autowired
    DroneControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Successful new drone registration")
    void registerDrone() throws Exception {
        DroneLightDto droneLightDto = new DroneLightDto("test", Model.CRUISERWEIGHT,  24, 500L);
        String requestBody = new ObjectMapper().writeValueAsString(droneLightDto);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_API_DRONE + "/register")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("11"));
    }

    @Test
    @DisplayName("Fail new drone registration - too long serial number")
    void registerDroneFail() throws Exception {
        DroneLightDto droneLightDto = new DroneLightDto("testtesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttesttest", Model.CRUISERWEIGHT, 90, 500L);
        String requestBody = new ObjectMapper().writeValueAsString(droneLightDto);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_API_DRONE + "/register")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Max length of serial number is exceeded (100 symbols)"));
    }

    @Test
    @DisplayName("Fail new drone registration - wrong battery range")
    void registerDroneFailBattery() throws Exception {
        DroneLightDto droneLightDto = new DroneLightDto("test", Model.CRUISERWEIGHT, 190, 500L);
        String requestBody = new ObjectMapper().writeValueAsString(droneLightDto);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_API_DRONE + "/register")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Battery level is invalid"));
    }

    @Test
    @DisplayName("Fail new drone registration - weight limit exceeded")
    void registerDroneFailWeight() throws Exception {
        DroneLightDto droneLightDto = new DroneLightDto("test", Model.CRUISERWEIGHT, 10, 1500L);
        String requestBody = new ObjectMapper().writeValueAsString(droneLightDto);
        mockMvc.perform(MockMvcRequestBuilders.post(URL_API_DRONE + "/register")
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Max weight limit is exceeded (500 gram)"));
    }

    @Test
    @DisplayName("Full load")
    void loadDroneFull() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(List.of(1, 2));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_API_DRONE + "/load/{droneId}", 10L)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Drone 10 loaded. Items loaded 2 pcs"));
    }

    @Test
    @DisplayName("Full load too much items")
    void loadDroneFullTooMuch() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(List.of(1, 2, 4, 5));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_API_DRONE + "/load/{droneId}", 1L)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Drone 1 is full. Items loaded 2 pcs, items left 2 pcs"));
    }

    @Test
    @DisplayName("Unsuccessful load low battery")
    void loadDroneLowBattery() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(List.of(1, 2, 4));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_API_DRONE + "/load/{droneId}", 9L)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Drone 9 battery level is 15%. Drone cannot be loaded when battery is lower then 25%"));
    }

    @Test
    @DisplayName("Unsuccessful load medication not found")
    void loadDroneWrongMedication() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(List.of(100));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_API_DRONE + "/load/{droneId}", 7L)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Medication with id 100 not found"));
    }

    @Test
    @DisplayName("Unsuccessful load wrong status")
    void loadDroneUnavailable() throws Exception {
        String requestBody = new ObjectMapper().writeValueAsString(List.of(1, 2, 4));
        mockMvc.perform(MockMvcRequestBuilders.put(URL_API_DRONE + "/load/{droneId}", 2L)
                        .content(requestBody).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Drone 2 unavailable to load - status DELIVERING"));
    }

    @Test
    @DisplayName("Successful get list of loaded items")
    void getLoadedItems() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/check/{droneId}", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(2)))
                .andExpect(jsonPath("$.[0].id", is(4)))
                .andExpect(jsonPath("$.[0].name", is("Medication 4")))
                .andExpect(jsonPath("$.[0].weight", is(200)))
                .andExpect(jsonPath("$.[0].code", is("CODE4")));
    }

    @Test
    @DisplayName("Items is absent")
    void getLoadedItemsEmpty() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/check/{droneId}", 1))
                .andDo(print())
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.[*]", hasSize(0)));
    }

    @Test
    @DisplayName("Successful get list of available drones")
    void getAvailableDrones() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(3)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].serialNumber", is("DRONE001")))
                .andExpect(jsonPath("$.[0].model", is("LIGHTWEIGHT")))
                .andExpect(jsonPath("$.[0].weightLimit", is(200)))
                .andExpect(jsonPath("$.[0].batteryCapacity", is(100)))
                .andExpect(jsonPath("$.[0].droneState", is("IDLE")));
    }

    @Test
    @DisplayName("Check battery level bad request")
    void checkBattery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/checkBattery/{droneId}", "wrongType"))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Successful check battery level")
    void testCheckBattery() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/checkBattery/{droneId}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("100"));
    }

    @Test
    @DisplayName("Check battery level drone not found")
    void testCheckBatteryNotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_DRONE + "/checkBattery/{droneId}", 100))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Drone with id 100 not found"));
    }
}