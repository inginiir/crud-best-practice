package com.kalita.drone.controllers.dto;

import com.kalita.drone.entities.Model;
import com.kalita.drone.entities.State;

import java.util.List;

public record DroneDto(Long id,
                       String serialNumber,
                       Model model,
                       Long weightLimit,
                       Integer batteryCapacity,
                       State droneState,
                       List<MedicationDto> items
) {
}
