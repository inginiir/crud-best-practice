package com.kalita.drone.controllers;

import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.services.MedicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/medication")
@RequiredArgsConstructor
public class MedicationController {

    private final MedicationService medicationService;

    @GetMapping("/list")
    public ResponseEntity<List<MedicationDto>> getMedicationList() {
        List<MedicationDto> medicationList = medicationService.getMedicationList();
        return ResponseEntity.ok(medicationList);
    }
}
