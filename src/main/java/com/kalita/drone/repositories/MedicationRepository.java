package com.kalita.drone.repositories;

import com.kalita.drone.entities.Medication;
import org.springframework.data.repository.CrudRepository;

public interface MedicationRepository extends CrudRepository<Medication, Long> {
}
