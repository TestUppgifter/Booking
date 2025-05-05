package com.example.booking.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class LoginResponse {
    private String message;

    public LoginResponse(String message) {
        this.message = message;
    }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
