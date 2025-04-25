package com.example.booking.model;

import org.springframework.http.HttpStatus;

public enum RegistrationError {
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT),
    INVALID_INPUT(HttpStatus.BAD_REQUEST),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus status;

    RegistrationError(HttpStatus status) {
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
