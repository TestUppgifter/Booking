package com.example.booking.controller;

import com.example.booking.DTO.LoginDTO;
import com.example.booking.DTO.LoginResponse;
import com.example.booking.DTO.UserRegistrationDTO;
import com.example.booking.model.RegistrationException;
import com.example.booking.model.RegistrationResponse;
import com.example.booking.model.Role;
import com.example.booking.model.User;
import com.example.booking.service.CustomUserDetailsService;
import com.example.booking.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


import javax.management.InstanceAlreadyExistsException;
import java.util.Collections;
import java.util.InputMismatchException;

@RestController
@RequestMapping("/api/auth")
@Validated
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService; // ***
    private final PasswordEncoder passwordEncoder;  // *****
    private final HttpServletRequest request;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          CustomUserDetailsService customUserDetailsService,
                          PasswordEncoder passwordEncoder, HttpServletRequest request) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.request = request;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO loginDTO) {
        System.out.println("******* Received username: " + loginDTO.getUsername());  // **********************
        System.out.println("******* Received passw: " + loginDTO.getPassword());  // **********************

        if (loginDTO.getUsername() == null || loginDTO.getUsername().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("error", "Username cannot be empty"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsername(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            HttpSession session = request.getSession(true);
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            return ResponseEntity.ok()
                    .body(Collections.singletonMap("username", loginDTO.getUsername()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials"));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@Valid @RequestBody UserRegistrationDTO userDTO) {

        try {
            User user = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegistrationResponse(
                            null,
                            "User created successfully",
                            user
                    ));
        } catch (InputMismatchException e) {
            return ResponseEntity.badRequest()
                    .body(new RegistrationResponse(
                            "Validation error",
                            e.getMessage(),
                            null
                    ));
        } catch (InstanceAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new RegistrationResponse(
                            "Conflict",
                            e.getMessage(),
                            null
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new RegistrationResponse(
                            "Server error",
                            "An unexpected error occurred",
                            null
                    ));
        }
    }

    @GetMapping("/check")
    public ResponseEntity<?> checkAuthentication(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return ResponseEntity.ok().body(Collections.singletonMap("username", authentication.getName()));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}