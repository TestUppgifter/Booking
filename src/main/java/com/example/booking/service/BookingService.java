package com.example.booking.service;

import com.example.booking.DTO.BookingRequest;
import com.example.booking.model.*;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.MachineRepository;
import com.example.booking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final MachineRepository machineRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, MachineRepository machineRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.machineRepository = machineRepository;
        this.userRepository = userRepository;
    }

    public Booking createBooking(BookingRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Machine machine = machineRepository.findById(request.machineId())
                .orElseThrow(() -> new RuntimeException("Machine not found"));

        if (!machine.getStatus().equals("AVAILABLE")) {
            throw new IllegalStateException("Machine is not available");
        }

        LocalDateTime now = LocalDateTime.now();
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMachine(machine);
        booking.setStartTime(now);
        booking.setEndTime(now.plusMinutes(request.duration()));
        booking.setActive(true);

        machine.setStatus("IN_USE");
        machine.setAvailableAt(booking.getEndTime());

        machineRepository.save(machine);
        return bookingRepository.save(booking);
    }

    public void cancelBooking(Long bookingId, String userEmail) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("Unauthorized cancellation attempt");
        }

        booking.setActive(false);
        Machine machine = booking.getMachine();
        machine.setStatus("AVAILABLE");
        machine.setAvailableAt(null);

        bookingRepository.save(booking);
        machineRepository.save(machine);
    }

    public List<Booking> getUserBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return bookingRepository.findByUserAndActiveTrue(user);
    }
}