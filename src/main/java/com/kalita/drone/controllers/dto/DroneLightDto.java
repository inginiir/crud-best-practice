package com.kalita.drone.controllers.dto;

import com.kalita.drone.entities.Model;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Range;

public record DroneLightDto(@Size(max = 100, message = "{drone.serialnumber.length}")
                            String serialNumber,
                            Model model,
                            @Range(min = 0, max = 100, message = "{drone.battery.invalid}")
                            Integer batteryCapacity,
                            @Max(value = 500, message = "{drone.weight.limit}")
                            Long weightLimit
) {
}
