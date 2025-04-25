package com.example.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "machines")
@AllArgsConstructor
@NoArgsConstructor
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;

    @Enumerated(EnumType.STRING)
    private MachineStatus status;

    @OneToMany(mappedBy = "machine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings;

    public Long getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public MachineStatus getStatus() {
        return status;
    }

    public void setStatus(MachineStatus status) {
        this.status = status;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}