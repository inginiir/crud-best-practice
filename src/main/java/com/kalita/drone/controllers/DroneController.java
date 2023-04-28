package com.kalita.drone.controllers;

import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.services.DroneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/drone")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerDrone(@RequestBody DroneDto drone) {
        Long id = droneService.registerDrone(drone);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/load/{droneId}")
    public ResponseEntity<Long> loadDrone(@PathVariable("droneId") Long droneId, @RequestBody List<Long> itemsId) {
        Long numberLoadedItems = droneService.loadDrone(droneId, itemsId);
        return ResponseEntity.ok(numberLoadedItems);
    }

    @GetMapping("/check/{droneId}")
    public ResponseEntity<List<MedicationDto>> getLoadedItems(@PathVariable("droneId") Long droneId) {
        List<MedicationDto> medicationDtoList = droneService.checkLoadedMedications(droneId);
        return ResponseEntity.ok(medicationDtoList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DroneDto>> getAvailableDrones() {
        List<DroneDto> availableDrones = droneService.getAvailableDrones();
        return ResponseEntity.ok(availableDrones);
    }

    @GetMapping("/checkBattery/{droneId}")
    public ResponseEntity<Integer> checkBattery(@PathVariable("droneId") Long droneId) {
        Integer percentageLeft = droneService.checkBatteryLevel(droneId);
        return ResponseEntity.ok(percentageLeft);
    }

    @Scheduled(cron = "0 0 * * * *")
    public void checkBattery() {
        log.info("Running scheduled task to check drone battery levels...");

        List<DroneLightDto> drones = droneService.getAllDrones();
        drones.forEach(drone -> {

        });
        log.info("Checking drone battery levels finished");
    }
}
