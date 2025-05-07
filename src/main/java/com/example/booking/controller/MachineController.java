package com.example.booking.controller;

import com.example.booking.model.Booking;
import com.example.booking.model.Machine;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MachineController {

    @GetMapping("/machines")
    public List<Machine> getMachines() {
        // Return list of washing machines
        return new ArrayList<>();
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(/*@RequestBody BookingRequest request*/) {
        // Create booking logic
        return null;
    }

    @GetMapping("/bookings")
    public List<Booking> getUserBookings() {
        // Get user's bookings
        new ArrayList<>();
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable String id) {
        // Cancel booking logic
        return null;
    }
}
