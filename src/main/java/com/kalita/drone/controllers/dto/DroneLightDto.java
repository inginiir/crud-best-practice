package com.kalita.drone.controllers.dto;

import com.kalita.drone.entities.Model;
import com.kalita.drone.entities.State;

public record DroneLightDto(String serialNumber,
                            Model model,
                            Integer batteryCapacity,
                            State droneState
) {
}
