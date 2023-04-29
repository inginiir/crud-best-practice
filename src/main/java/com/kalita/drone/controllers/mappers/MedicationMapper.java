package com.kalita.drone.controllers.mappers;

import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.entities.Medication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicationMapper {

    @Mapping(target = "name", source = "medicationName")
    MedicationDto mapToDto(Medication medication);
}
