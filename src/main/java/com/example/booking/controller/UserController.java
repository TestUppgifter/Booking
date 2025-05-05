package com.example.booking.controller;

import com.example.booking.model.User;
import com.example.booking.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@PreAuthorize("isAuthenticated()")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(@AuthenticationPrincipal User user) {
        System.out.println("SecurityContext: " + SecurityContextHolder.getContext());
        System.out.println("Authentication: " + SecurityContextHolder.getContext().getAuthentication());
        System.out.println("Principal: " + SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        // Check if user is authenticated
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            System.out.println("Error accessing /me: " + e.getMessage());  // ************
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
