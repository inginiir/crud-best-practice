package com.kalita.drone.repositories;

import com.kalita.drone.entities.Drone;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DroneRepository extends CrudRepository<Drone, Long> {

    @Query(value = "SELECT * FROM drones " +
            "WHERE battery_capacity >= 25 AND drone_state = 'IDLE'", nativeQuery = true)
    List<Drone> findAvailableDrones();
}
