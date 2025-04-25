package com.example.booking.controller;

import com.example.booking.DTO.UserRegistrationDTO;
import com.example.booking.model.RegistrationException;
import com.example.booking.model.Role;
import com.example.booking.model.User;
import com.example.booking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.management.InstanceAlreadyExistsException;
import java.util.InputMismatchException;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody UserRegistrationDTO userDTO) {
        try {
            User user = userService.createUser(userDTO);
            return ResponseEntity.ok(user);
        } catch (RegistrationException e) {
            return ResponseEntity.status(e.getError().getStatus())
                    .body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}