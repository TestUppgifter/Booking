package com.example.booking.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record BookingRequest(
        @NotNull(message = "Machine ID is required")
        Long machineId,

        @Min(value = 15, message = "Minimum booking duration is 15 minutes")
        @Max(value = 180, message = "Maximum booking duration is 3 hours")
        int duration
) {}