package com.example.booking.controller;

import com.example.booking.DTO.BookingRequest;
import com.example.booking.model.Booking;
import com.example.booking.model.Machine;
import com.example.booking.service.MachineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }


    @GetMapping
    public ResponseEntity<List<Machine>> getAllMachines() {
        return ResponseEntity.ok(machineService.getAllMachines());
    }

    @PostMapping("/bookings")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequest request) {
        // Create booking logic
        return null;
    }

    @GetMapping("/bookings")
    public List<Booking> getUserBookings() {
        // Get user's bookings
        return new ArrayList<>();
    }

    @DeleteMapping("/bookings/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable String id) {
        // Cancel booking logic
        return null;
    }
}
