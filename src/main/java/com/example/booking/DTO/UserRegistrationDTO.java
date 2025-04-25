package com.example.booking.DTO;

import lombok.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRegistrationDTO {

    private String email;
    private String password;
    private String role;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole(){
        return role;
    }

    public void setPassword(String password) {
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Getters and setters
}
