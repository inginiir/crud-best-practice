package com.kalita.drone.controllers.mappers;

import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.entities.Drone;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = MedicationMapper.class)
public interface DroneMapper {

    DroneDto mapToDto(Drone medication);

    DroneLightDto mapToLightDto(Drone medication);

    Drone mapToEntity(DroneLightDto medication);
}
