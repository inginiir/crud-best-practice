package com.kalita.drone.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "drones")
public class Drone {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "drone_id_generator")
    @SequenceGenerator(name = "drone_id_generator", sequenceName = "drone_id_generator", allocationSize = 1)
    private Long id;
    private String serialNumber;
    @Enumerated(EnumType.STRING)
    private Model model;
    private Long weightLimit;
    private Integer batteryCapacity;
    @Enumerated(EnumType.STRING)
    private State droneState;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "drone_medication_id",
            joinColumns = {@JoinColumn(name = "drone_id")},
            inverseJoinColumns = {@JoinColumn(name = "medication_id")}
    )
    private List<Medication> items;
}
