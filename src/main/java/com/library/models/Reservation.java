package com.library.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a book reservation.
 */
public class Reservation {
    private String reservationId;
    private String patronId;
    private String isbn;
    private LocalDate reservationDate;
    private LocalDate expiryDate;
    private ReservationStatus status;

    public Reservation(String reservationId, String patronId, String isbn) {
        this.reservationId = reservationId;
        this.patronId = patronId;
        this.isbn = isbn;
        this.reservationDate = LocalDate.now();
        this.expiryDate = LocalDate.now().plusDays(7); // 7 days to collect
        this.status = ReservationStatus.ACTIVE;
    }

    // Getters
    public String getReservationId() {
        return reservationId;
    }

    public String getPatronId() {
        return patronId;
    }

    public String getIsbn() {
        return isbn;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    // Setters
    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    // Business methods
    public boolean isExpired() {
        return LocalDate.now().isAfter(expiryDate);
    }

    public boolean isActive() {
        return status == ReservationStatus.ACTIVE && !isExpired();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId);
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "reservationId='" + reservationId + '\'' +
                ", patronId='" + patronId + '\'' +
                ", isbn='" + isbn + '\'' +
                ", reservationDate=" + reservationDate +
                ", expiryDate=" + expiryDate +
                ", status=" + status +
                '}';
    }
}
