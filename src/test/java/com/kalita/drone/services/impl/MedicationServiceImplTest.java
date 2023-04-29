package com.kalita.drone.services.impl;

import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.controllers.mappers.MedicationMapper;
import com.kalita.drone.entities.Medication;
import com.kalita.drone.repositories.MedicationRepository;
import com.kalita.drone.services.MedicationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MedicationServiceImplTest {

    private final MedicationMapper mapper = Mockito.mock(MedicationMapper.class);
    private final MedicationRepository repository = Mockito.mock(MedicationRepository.class);
    private final MedicationService medicationService = new MedicationServiceImpl(repository, mapper);

    @Test
    void getMedicationList() {
        MedicationDto medicationDto = new MedicationDto(1L, "test", 200, "test");
        doReturn(medicationDto)
                .when(mapper).mapToDto(any(Medication.class));
        List<Medication> medications = createTestData(3);
        doReturn(medications)
                .when(repository).findAll();
        List<MedicationDto> medicationList = medicationService.getMedicationList();
        assertEquals(3, medicationList.size());
        verify(mapper, times(3)).mapToDto(any(Medication.class));
        verify(repository, times(1)).findAll();
    }

    private List<Medication> createTestData(int quantity) {
        List<Medication> medications = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            Medication medication = new Medication();
            medication.setId((long) i);
            medication.setMedicationName("test" + i);
            medication.setCode("test" + i);
            medication.setWeight(i * 10);
            medications.add(medication);
        }
        return medications;
    }
}