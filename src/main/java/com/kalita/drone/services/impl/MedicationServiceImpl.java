package com.kalita.drone.services.impl;

import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.controllers.mappers.MedicationMapper;
import com.kalita.drone.entities.Medication;
import com.kalita.drone.repositories.MedicationRepository;
import com.kalita.drone.services.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class MedicationServiceImpl implements MedicationService {

    private final MedicationRepository medicationRepository;
    private final MedicationMapper medicationMapper;

    @Override
    public List<MedicationDto> getMedicationList() {
        Iterable<Medication> medications = medicationRepository.findAll();
        return StreamSupport.stream(medications.spliterator(), false)
                .map(medicationMapper::mapToDto).toList();
    }
}
