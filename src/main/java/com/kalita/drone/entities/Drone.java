package com.kalita.drone.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "drones")
public class Drone {

    @Id
    private Long id;
    @Size(max = 100, message = "{drone.serialnumber.length}")
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private Model model;
    @Max(value = 500, message = "{drone.weight.limit}")
    private Long weightLimit;
    @Range(min = 0, max = 100, message = "{drone.battery.invalid}")
    private Integer batteryCapacity;
    @Enumerated(EnumType.STRING)
    private State drone_state;
    @OneToMany(mappedBy = "drone", fetch = FetchType.LAZY)
    private List<Medication> items;
}
