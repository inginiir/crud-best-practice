package com.kalita.drone.controllers;

import com.kalita.drone.configuration.MessageConfig;
import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.services.AuditService;
import com.kalita.drone.services.DroneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kalita.drone.configuration.MessageConfig.Code.DRONE_IS_FULL;
import static com.kalita.drone.configuration.MessageConfig.Code.DRONE_LOADED;

@Slf4j
@RestController
@RequestMapping("/api/drone")
@RequiredArgsConstructor
public class DroneController {

    private final DroneService droneService;
    private final MessageSource messageSource;
    private final AuditService auditService;

    @PostMapping("/register")
    public ResponseEntity<Long> registerDrone(@Valid @RequestBody DroneLightDto drone) {
        Long id = droneService.registerDrone(drone);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/load/{droneId}")
    public ResponseEntity<String> loadDrone(@PathVariable("droneId") Long droneId, @RequestBody List<Long> itemsId) {
        Long numberLoadedItems = droneService.loadDrone(droneId, itemsId);
        String loadingInfo = getLoadingInfo(droneId, itemsId.size(), numberLoadedItems);
        return ResponseEntity.ok(loadingInfo);
    }

    @GetMapping("/check/{droneId}")
    public ResponseEntity<List<MedicationDto>> getLoadedItems(@PathVariable("droneId") Long droneId) {
        List<MedicationDto> medicationList = droneService.checkLoadedMedications(droneId);
        return medicationList.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(medicationList)
                : ResponseEntity.ok(medicationList);
    }

    @GetMapping("/list")
    public ResponseEntity<List<DroneDto>> getAvailableDrones() {
        List<DroneDto> availableDrones = droneService.getAvailableDrones();
        return availableDrones.isEmpty() ? ResponseEntity.status(HttpStatus.NO_CONTENT).body(availableDrones)
                : ResponseEntity.ok(availableDrones);
    }

    @GetMapping("/checkBattery/{droneId}")
    public ResponseEntity<Integer> checkBattery(@PathVariable("droneId") Long droneId) {
        Integer percentageLeft = droneService.checkBatteryLevel(droneId);
        return ResponseEntity.ok(percentageLeft);
    }

    @Scheduled(cron = "0 * * * * *")
    public void checkBattery() {
        log.info("Running scheduled task to check drone battery levels...");
        List<DroneLightDto> drones = droneService.getAllDrones();
        drones.forEach(drone -> auditService.logAuditEvent(String.format("Checking drone %s model %s. Battery level is %s",
                drone.serialNumber(), drone.model(), drone.batteryCapacity())));
        log.info("Checking drone battery levels finished");
    }

    private String getLoadingInfo(Long droneId, long countItems, long countLoaded) {
        return countLoaded == countItems ? getMessage(DRONE_LOADED, droneId, countLoaded)
                : getMessage(DRONE_IS_FULL, droneId, countLoaded, countItems - countLoaded);
    }

    private String getMessage(MessageConfig.Code code, Object... args) {
        return messageSource.getMessage(code.getValue(), args, LocaleContextHolder.getLocale());
    }
}
