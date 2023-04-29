package com.kalita.drone.services.impl;

import com.kalita.drone.configuration.MessageConfig;
import com.kalita.drone.controllers.dto.DroneDto;
import com.kalita.drone.controllers.dto.DroneLightDto;
import com.kalita.drone.controllers.dto.MedicationDto;
import com.kalita.drone.controllers.mappers.DroneMapper;
import com.kalita.drone.controllers.mappers.MedicationMapper;
import com.kalita.drone.entities.Drone;
import com.kalita.drone.entities.Medication;
import com.kalita.drone.entities.State;
import com.kalita.drone.exceptions.DroneStatusException;
import com.kalita.drone.exceptions.LowBatteryException;
import com.kalita.drone.exceptions.NotFoundException;
import com.kalita.drone.repositories.DroneRepository;
import com.kalita.drone.repositories.MedicationRepository;
import com.kalita.drone.services.DroneService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import static com.kalita.drone.configuration.MessageConfig.Code.*;
import static com.kalita.drone.entities.State.IDLE;

@Slf4j
@Service
@RequiredArgsConstructor
public class DroneServiceImpl implements DroneService {

    private final DroneRepository droneRepository;
    private final MedicationRepository medicationRepository;
    private final DroneMapper droneMapper;
    private final MedicationMapper medicationMapper;
    private final MessageSource messageSource;

    @Override
    @Transactional
    public Long registerDrone(DroneLightDto droneDto) {
        Drone drone = droneMapper.mapToEntity(droneDto);
        drone.setDroneState(IDLE);
        Drone savedDrone = droneRepository.save(drone);
        return savedDrone.getId();
    }

    @Override
    @Transactional
    public Long loadDrone(Long id, List<Long> items) {
        Drone drone = getDrone(id);
        checkAvailability(drone);
        drone.setDroneState(State.LOADING);
        droneRepository.save(drone);
        List<Medication> medications = getMedications(items);
        Long message = loadItems(drone, medications);
        drone.setDroneState(State.LOADED);
        droneRepository.save(drone);
        return message;
    }

    @Override
    public List<MedicationDto> checkLoadedMedications(Long id) {
        Drone drone = getDrone(id);
        List<Medication> items = drone.getItems();
        return items.stream().map(medicationMapper::mapToDto).toList();
    }

    @Override
    public List<DroneDto> getAvailableDrones() {
        List<Drone> availableDrones = droneRepository.findAvailableDrones();
        return availableDrones.stream().map(droneMapper::mapToDto).toList();
    }

    @Override
    public List<DroneLightDto> getAllDrones() {
        Iterable<Drone> drones = droneRepository.findAll();
        return StreamSupport.stream(drones.spliterator(), false)
                .map(droneMapper::mapToLightDto)
                .toList();
    }

    @Override
    public Integer checkBatteryLevel(Long id) {
        Drone drone = getDrone(id);
        return drone.getBatteryCapacity();
    }

    private Long loadItems(Drone drone, List<Medication> medications) {
        long countLoaded = 0L;
        long currentWeight = 0L;
        Long weightLimit = drone.getWeightLimit();
        List<Medication> preparedItems = new ArrayList<>();
        for (Medication medication : medications) {
            Integer weight = medication.getWeight();
            if (currentWeight + weight <= weightLimit) {
                countLoaded++;
                currentWeight += weight;
                preparedItems.add(medication);
            } else {
                break;
            }
        }
        drone.setItems(preparedItems);
        return countLoaded;
    }

    private void checkAvailability(Drone drone) {
        if (drone.getBatteryCapacity() < 25) {
            throw new LowBatteryException(getMessage(LOW_BATTERY, drone.getId(), drone.getBatteryCapacity()));
        } else if (!IDLE.equals(drone.getDroneState())) {
            throw new DroneStatusException(getMessage(UNAVAILABLE_STATUS, drone.getId(), drone.getDroneState()));
        }
    }

    private Drone getDrone(Long id) {
        return droneRepository.findById(id).orElseThrow(() -> new NotFoundException(getMessage(DRONE_NOT_FOUND, id)));
    }

    private List<Medication> getMedications(List<Long> items) {
        return items.stream().map(medicineId -> medicationRepository.findById(medicineId).orElseThrow(()
                        -> new NotFoundException(getMessage(MEDICATION_NOT_FOUND, medicineId))))
                .toList();
    }

    private String getMessage(MessageConfig.Code code, Object... id) {
        return messageSource.getMessage(code.getValue(), id, LocaleContextHolder.getLocale());
    }
}
