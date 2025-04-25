package com.example.booking.service;

import com.example.booking.model.*;
import com.example.booking.repository.BookingRepository;
import com.example.booking.repository.MachineRepository;
import com.example.booking.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.InputMismatchException;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final MachineRepository machineRepository;

    public BookingService(BookingRepository bookingRepository,
                          UserRepository userRepository,
                          MachineRepository machineRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.machineRepository = machineRepository;
    }

    public Booking createBooking(Long userId, Long machineId,
                                 LocalDateTime startTime, int durationMinutes) {
        validateInput(userId, machineId, startTime, durationMinutes);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Machine machine = machineRepository.findById(machineId)
                .orElseThrow(() -> new ObjectNotFoundException(machineId, "Machine not found"));

        validateBooking(machine);

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setMachine(machine);
        booking.setStartTime(startTime);
        booking.setEndTime(startTime.plusMinutes(durationMinutes));
        booking.setStatus(BookingStatus.PENDING);

        return bookingRepository.save(booking);
    }

    private void validateInput(Long userId, Long machineId, LocalDateTime startTime, int durationMinutes) {
        if (userId == null || machineId == null) {
            throw new InputMismatchException("User ID and machine ID are required");
        }
        if (startTime == null || startTime.isBefore(LocalDateTime.now())) {
            throw new InputMismatchException("Invalid start time");
        }
        if (durationMinutes <= 0) {
            throw new InputMismatchException("Duration must be positive");
        }
    }

    private void validateBooking(Machine machine) {
        if (machine.getStatus() != MachineStatus.AVAILABLE) {
            throw new InputMismatchException("Machine is not available");
        }

        if (bookingRepository.existsById(machine.getId())) {
            throw new InputMismatchException("Machine is already booked in this time slot");
        }
    }
}