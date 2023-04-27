package com.kalita.drone.services.impl;

import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.services.DroneService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DroneServiceImpl implements DroneService {


    @Override
    public Long registerDrone(DroneDto drone) {
        return null;
    }

    @Override
    public Long loadDrone(Long id, List<Long> items) {
        return 1L;
    }

    @Override
    public List<MedicationDto> checkLoadedMedications(Long id) {
        return null;
    }

    @Override
    public List<DroneDto> getAvailableDrones() {
        return null;
    }

    @Override
    public List<DroneDto> getAllDrones() {
        return null;
    }

    @Override
    public Integer checkBatteryLevel(Long id) {
        return 0;
    }
}
