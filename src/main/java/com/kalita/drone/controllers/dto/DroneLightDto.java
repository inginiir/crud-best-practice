package com.kalita.drone.controllers.dto;

import com.kalita.drone.entities.Model;

public record DroneLightDto(String serialNumber,
                            Model model,
                            Integer batteryCapacity,
                            Long weightLimit
) {
}
