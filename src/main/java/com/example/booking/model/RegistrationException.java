package com.example.booking.model;

public class RegistrationException extends RuntimeException {
    private final RegistrationError error;

    public RegistrationException(String message, RegistrationError error) {
        super(message);
        this.error = error;
    }

    public RegistrationError getError() {
        return error;
    }
}