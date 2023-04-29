package com.kalita.drone.controllers;

import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.entities.Model;
import com.kalita.drone.services.AuditService;
import com.kalita.drone.services.DroneService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.MessageSource;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class DroneControllerSchedulerTest {

    private final DroneService droneService = Mockito.mock(DroneService.class);
    private final MessageSource messageSource = Mockito.mock(MessageSource.class);
    private final AuditService auditService = Mockito.mock(AuditService.class);
    private final DroneController droneController = new DroneController(droneService, messageSource, auditService);

    @Test
    void checkBattery() {
        List<DroneLightDto> drones = createListOfDrone(10);
        doReturn(drones)
                .when(droneService).getAllDrones();
        droneController.checkBattery();
        verify(droneService, times(1)).getAllDrones();
        verify(auditService, times(10)).logAuditEvent(anyString());
    }

    private List<DroneLightDto> createListOfDrone(int amount) {
        List<DroneLightDto> droneDtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            droneDtoList.add(new DroneLightDto("test", Model.CRUISERWEIGHT,
                    10 * i, 10L * i));
        }
        return droneDtoList;
    }
}