package com.kalita.drone.controllers.dto;

public record MedicationDto(Long id,
                            String name,
                            Integer weight,
                            String code
) {
}
