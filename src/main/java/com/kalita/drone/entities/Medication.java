package com.kalita.drone.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "medications")
public class Medication {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medication_id_generator")
    @SequenceGenerator(name = "medication_id_generator", sequenceName = "medication_id_generator", allocationSize = 1)
    private Long id;
    private String medicationName;
    private Integer weight;
    private String code;
    private String imageUrl;
}
