package com.kalita.drone.controllers.dto;

import jakarta.validation.constraints.Pattern;

public record MedicationDto(Long id,
                            @Pattern(regexp = "^[a-zA-Z\\d-_]+$", message = "{medication.name.constraints}")
                            String name,
                            Integer weight,
                            @Pattern(regexp = "^[A-Z\\d_]+$", message = "{medication.code.constraints}")
                            String code
) {
}
