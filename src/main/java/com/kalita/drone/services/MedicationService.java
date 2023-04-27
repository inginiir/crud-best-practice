package com.kalita.drone.services;

import com.kalita.drone.controllers.dto.MedicationDto;

import java.util.List;

public interface MedicationService {

    List<MedicationDto> getMedicationList();
}
