package com.kalita.drone.exceptions;

public class LowBatteryException extends RuntimeException {
    public LowBatteryException(String message) {
        super(message);
    }
}
