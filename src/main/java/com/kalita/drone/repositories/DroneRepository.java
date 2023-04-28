package com.kalita.drone.repositories;

import com.kalita.drone.entities.Drone;
import org.springframework.data.repository.CrudRepository;

public interface DroneRepository extends CrudRepository<Drone, Long> {
}
