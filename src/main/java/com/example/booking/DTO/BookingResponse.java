package com.example.booking.DTO;

import java.time.LocalDateTime;

public record BookingResponse(
        Long id,
        Long machineId,
        String machineType,
        LocalDateTime startTime,
        LocalDateTime endTime,
        String status
) {}
