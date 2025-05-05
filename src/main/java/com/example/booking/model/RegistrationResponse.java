package com.example.booking.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class RegistrationResponse {
    private String error;
    private String message;
    private User user;

    public RegistrationResponse(String error, String message, User user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

}