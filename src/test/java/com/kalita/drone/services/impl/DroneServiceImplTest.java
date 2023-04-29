package com.kalita.drone.services.impl;

import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.controllers.mappers.DroneMapper;
import com.kalita.drone.controllers.mappers.MedicationMapper;
import com.kalita.drone.entities.Drone;
import com.kalita.drone.entities.Medication;
import com.kalita.drone.entities.Model;
import com.kalita.drone.entities.State;
import com.kalita.drone.exceptions.LowBatteryException;
import com.kalita.drone.exceptions.NotFoundException;
import com.kalita.drone.repositories.DroneRepository;
import com.kalita.drone.repositories.MedicationRepository;
import com.kalita.drone.services.DroneService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DroneServiceImplTest {

    private final DroneMapper droneMapper = Mockito.mock(DroneMapper.class);
    private final MedicationMapper medicationMapper = Mockito.mock(MedicationMapper.class);
    private final DroneRepository droneRepository = Mockito.mock(DroneRepository.class);
    private final MessageSource messageSource = Mockito.mock(MessageSource.class);
    private final MedicationRepository medicationRepository = Mockito.mock(MedicationRepository.class);
    private final DroneService droneService = new DroneServiceImpl(droneRepository, medicationRepository,
            droneMapper, medicationMapper, messageSource);

    @Test
    @DisplayName("Successful registration")
    void registerDrone() {
        doReturn(createTestDrone(null))
                .when(droneMapper).mapToEntity(any(DroneLightDto.class));
        doReturn(createTestDrone(1L))
                .when(droneRepository).save(any(Drone.class));
        DroneLightDto droneDto = createLightDrone();
        Long droneId = droneService.registerDrone(droneDto);
        assertEquals(1L, droneId);
        verify(droneMapper, times(1)).mapToEntity(any(DroneLightDto.class));
        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Successful loading")
    void loadDrone() {
        Medication medication1 = createMedication(1L);
        Medication medication2 = createMedication(2L);
        Medication medication3 = createMedication(3L);
        doReturn(Optional.of(medication1))
                .when(medicationRepository).findById(1L);
        doReturn(Optional.of(medication2))
                .when(medicationRepository).findById(2L);
        doReturn(Optional.of(medication3))
                .when(medicationRepository).findById(3L);
        Drone drone = createTestDrone(1L);
        drone.setDroneState(State.IDLE);
        doReturn(Optional.of(drone))
                .when(droneRepository).findById(1L);
        doReturn("Drone {} loaded. Items loaded {}, items weight {}")
                .when(messageSource).getMessage(anyString(), any(), any());
        Long loaded = droneService.loadDrone(1L, List.of(1L, 2L, 3L));

        assertEquals(3, loaded);
        verify(medicationRepository, times(3)).findById(anyLong());
        verify(droneRepository, times(1)).findById(anyLong());
        verify(droneRepository, times(2)).save(any(Drone.class));
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }


    @Test
    @DisplayName("Overload, not all items is loaded")
    void loadDroneOverload() {
        Medication medication1 = createMedication(1L);
        Medication medication2 = createMedication(2L);
        Medication medication3 = createMedication(3L);
        doReturn(Optional.of(medication1))
                .when(medicationRepository).findById(1L);
        doReturn(Optional.of(medication2))
                .when(medicationRepository).findById(2L);
        doReturn(Optional.of(medication3))
                .when(medicationRepository).findById(3L);
        Drone drone = createTestDrone(1L);
        drone.setWeightLimit(100L);
        drone.setDroneState(State.IDLE);
        doReturn(Optional.of(drone))
                .when(droneRepository).findById(1L);
        doReturn("Drone {} loaded. Items loaded {}, items weight {}")
                .when(messageSource).getMessage(anyString(), any(), any());
        Long loaded = droneService.loadDrone(1L, List.of(1L, 2L, 3L));

        assertEquals(1, loaded);
        verify(medicationRepository, times(3)).findById(anyLong());
        verify(droneRepository, times(1)).findById(anyLong());
        verify(droneRepository, times(2)).save(any(Drone.class));
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Can't loaded - low battery")
    void loadDroneWrongState() {
        Drone drone = createTestDrone(1L);
        drone.setBatteryCapacity(10);
        doReturn(Optional.of(drone))
                .when(droneRepository).findById(1L);
        assertThrows(LowBatteryException.class, () -> droneService.loadDrone(1L, List.of(1L, 2L, 3L)));
        verify(medicationRepository, never()).findById(anyLong());
        verify(droneRepository, times(1)).findById(anyLong());
        verify(droneRepository, never()).save(any(Drone.class));
        verify(messageSource, times(1)).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Get list of medications for specific drone")
    void checkLoadedMedications() {
        Drone drone = createTestDrone(1L);
        Medication medication = createMedication(1L);
        drone.setItems(List.of(medication));
        doReturn(Optional.of(drone))
                .when(droneRepository).findById(1L);
        MedicationDto medicationDto = getMedicationDto();
        doReturn(medicationDto)
                .when(medicationMapper).mapToDto(any(Medication.class));
        List<MedicationDto> medicationDtoList = droneService.checkLoadedMedications(1L);
        assertEquals(1, medicationDtoList.size());
        verify(droneRepository, times(1)).findById(anyLong());
        verify(medicationMapper, times(1)).mapToDto(any(Medication.class));
    }

    @Test
    @DisplayName("Get available drones")
    void getAvailableDrones() {
        List<Drone> drones = createTestDroneList(10);
        doReturn(drones)
                .when(droneRepository).findAvailableDrones();
        DroneDto droneDto = createDroneDto(1L);
        doReturn(droneDto)
                .when(droneMapper).mapToDto(any());
        List<DroneDto> availableDrones = droneService.getAvailableDrones();
        assertEquals(10, availableDrones.size());
        verify(droneRepository, times(1)).findAvailableDrones();
        verify(droneMapper, times(10)).mapToDto(any(Drone.class));
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Getting list of drones for scheduler")
    void getAllDrones() {
        List<Drone> testDroneList = createTestDroneList(5);
        doReturn(testDroneList)
                .when(droneRepository).findAll();
        DroneLightDto lightDrone = createLightDrone();
        doReturn(lightDrone)
                .when(droneMapper).mapToLightDto(any(Drone.class));
        List<DroneLightDto> allDrones = droneService.getAllDrones();
        assertEquals(5, allDrones.size());
        verify(droneRepository, times(1)).findAll();
        verify(droneMapper, times(5)).mapToLightDto(any(Drone.class));
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Check battery level when drone exist")
    void checkBatteryLevel() {
        Drone drone = createTestDrone(1L);
        doReturn(Optional.of(drone))
                .when(droneRepository).findById(1L);
        Integer level = droneService.checkBatteryLevel(1L);
        assertEquals(29, level);
        verify(droneRepository, times(1)).findById(1L);
        verify(messageSource, never()).getMessage(anyString(), any(), any());
    }

    @Test
    @DisplayName("Check battery level when drone not found")
    void checkBatteryLevelFault() {
        assertThrows(NotFoundException.class, () -> droneService.checkBatteryLevel(1L));
        verify(droneRepository, times(1)).findById(1L);
        verify(messageSource, times(1)).getMessage(anyString(), any(), any());
    }

    private DroneLightDto createLightDrone() {
        return new DroneLightDto("test", Model.CRUISERWEIGHT, 29, 200L);
    }

    private Drone createTestDrone(Long id) {
        Drone drone = new Drone();
        drone.setId(id);
        drone.setBatteryCapacity(29);
        drone.setWeightLimit(600L);
        drone.setSerialNumber("test");
        drone.setModel(Model.CRUISERWEIGHT);
        drone.setDroneState(State.DELIVERED);
        return drone;
    }


    private DroneDto createDroneDto(Long id) {
        return new DroneDto(id, "test", Model.CRUISERWEIGHT, 500L, 80, State.DELIVERED);
    }

    private List<Drone> createTestDroneList(int quantity) {
        List<Drone> drones = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            drones.add(createTestDrone((long) i));
        }
        return drones;
    }

    private Medication createMedication(long id) {
        Medication medication = new Medication();
        medication.setId(id);
        medication.setCode("test");
        medication.setMedicationName("test");
        medication.setWeight((int) id * 100);
        return medication;
    }

    private MedicationDto getMedicationDto() {
        return new MedicationDto(1L, "test", 100, "test");
    }
}