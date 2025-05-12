package com.example.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "machines")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // e.g., "Washing Machine", "Dryer"
    private String status; // AVAILABLE, IN_USE
    private LocalDateTime availableAt;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL)
    private List<Booking> bookings = new ArrayList<>();

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getStatus() {
        return status;
    }

    public void setAvailableAt(LocalDateTime endTime) {
        this.availableAt = endTime;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    // Getters and Setters
}