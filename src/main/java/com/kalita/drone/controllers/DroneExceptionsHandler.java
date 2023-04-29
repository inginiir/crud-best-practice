package com.kalita.drone.controllers;

import com.kalita.drone.exceptions.DroneStatusException;
import com.kalita.drone.exceptions.LowBatteryException;
import com.kalita.drone.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static ch.qos.logback.core.CoreConstants.EMPTY_STRING;

@RestControllerAdvice
public class DroneExceptionsHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> notFoundExceptionHandler(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(DroneStatusException.class)
    public ResponseEntity<String> droneStatusExceptionHandler(DroneStatusException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(LowBatteryException.class)
    public ResponseEntity<String> lowBatteryExceptionHandler(LowBatteryException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> customValidationException(MethodArgumentNotValidException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();
        String detailMessage = EMPTY_STRING;
        if (fieldError != null) {
            detailMessage = fieldError.getDefaultMessage();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detailMessage);
    }
}
