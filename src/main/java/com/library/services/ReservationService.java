package com.library.services;

import com.library.models.Reservation;
import java.util.List;
import java.util.Optional;

/**
 * Interface for reservation management operations.
 */
public interface ReservationService {
    Reservation reserveBook(String patronId, String isbn);
    boolean cancelReservation(String reservationId);
    Optional<Reservation> findReservationById(String reservationId);
    List<Reservation> getReservationsByPatron(String patronId);
    List<Reservation> getReservationsByBook(String isbn);
    void processExpiredReservations();
}
