package com.kalita.drone.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.yml")
class MedicationControllerTest {

    private final static String URL_API_MEDICATION = "/api/medication";
    private final MockMvc mockMvc;

    @Autowired
    MedicationControllerTest(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    @DisplayName("Full list of medication")
    void getMedicationList() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(URL_API_MEDICATION + "/list", 2))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[*]", hasSize(20)))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", is("Medication 1")))
                .andExpect(jsonPath("$.[0].weight", is(100)))
                .andExpect(jsonPath("$.[0].code", is("CODE1")));
    }
}