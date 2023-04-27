package com.kalita.drone.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "medications")
public class Medication {

    @Id
    private Long id;
    @Pattern(regexp = "^[a-zA-Z\\d-_]+$", message = "{medication.name.constraints}")
    private String medicationName;
    private Integer weight;
    @Pattern(regexp = "^[A-Z\\d_]+$", message = "{medication.code.constraints}")
    private String code;
    private String imageUrl;
    @ManyToOne
    @JoinColumn(name = "drone_id", referencedColumnName = "id", nullable = false)
    private Drone drone;
}
