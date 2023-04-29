package com.kalita.drone.services;

import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.controllers.dto.MedicationDto;

import java.util.List;

public interface DroneService {

    Long registerDrone(DroneLightDto drone);

    Long loadDrone(Long id, List<Long> items);

    List<MedicationDto> checkLoadedMedications(Long id);

    List<DroneDto> getAvailableDrones();

    List<DroneLightDto> getAllDrones();

    Integer checkBatteryLevel(Long id);
}
