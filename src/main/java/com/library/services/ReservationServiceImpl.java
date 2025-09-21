package com.library.services;

import com.library.models.*;
import com.library.patterns.Subject;
import com.library.utils.IdGenerator;
import com.library.utils.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of ReservationService.
 * Extends Subject to implement Observer pattern for notifications.
 */
public class ReservationServiceImpl extends Subject implements ReservationService {
    private final BookService bookService;
    private final PatronService patronService;
    private final Map<String, Reservation> reservations;

    public ReservationServiceImpl(BookService bookService, PatronService patronService) {
        this.bookService = bookService;
        this.patronService = patronService;
        this.reservations = new ConcurrentHashMap<>();
    }

    @Override
    public Reservation reserveBook(String patronId, String isbn) {
        if (patronId == null || isbn == null) {
            throw new IllegalArgumentException("Patron ID and ISBN cannot be null");
        }

        // Validate patron and book existence
        Optional<Patron> patronOpt = patronService.findPatronById(patronId);
        Optional<Book> bookOpt = bookService.findBookByIsbn(isbn);

        if (patronOpt.isEmpty()) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }

        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }

        Book book = bookOpt.get();
        Patron patron = patronOpt.get();

        // Check if book is available (shouldn't reserve available books)
        if (book.isAvailable()) {
            throw new IllegalStateException("Book is available for immediate borrowing");
        }

        // Check if patron already has a reservation for this book
        boolean hasExistingReservation = reservations.values().stream()
                .anyMatch(r -> r.getPatronId().equals(patronId) && 
                              r.getIsbn().equals(isbn) && 
                              r.isActive());

        if (hasExistingReservation) {
            throw new IllegalStateException("Patron already has an active reservation for this book");
        }

        // Create reservation
        String reservationId = IdGenerator.generateReservationId();
        Reservation reservation = new Reservation(reservationId, patronId, isbn);
        reservations.put(reservationId, reservation);

        Logger.logInfo("Book reserved: " + book.getTitle() + " by " + patron.getName());
        notifyObservers("Reservation created for '" + book.getTitle() + "' by " + patron.getName());

        return reservation;
    }

    @Override
    public boolean cancelReservation(String reservationId) {
        if (reservationId == null) {
            throw new IllegalArgumentException("Reservation ID cannot be null");
        }

        Reservation reservation = reservations.get(reservationId);
        if (reservation == null) {
            Logger.logWarning("Attempted to cancel non-existent reservation: " + reservationId);
            return false;
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        Logger.logInfo("Reservation cancelled: " + reservationId);
        
        return true;
    }

    @Override
    public Optional<Reservation> findReservationById(String reservationId) {
        return Optional.ofNullable(reservations.get(reservationId));
    }

    @Override
    public List<Reservation> getReservationsByPatron(String patronId) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getPatronId().equals(patronId))
                .toList();
    }

    @Override
    public List<Reservation> getReservationsByBook(String isbn) {
        return reservations.values().stream()
                .filter(reservation -> reservation.getIsbn().equals(isbn) && 
                                      reservation.isActive())
                .sorted(Comparator.comparing(Reservation::getReservationDate))
                .toList();
    }

    @Override
    public void processExpiredReservations() {
        List<Reservation> expiredReservations = reservations.values().stream()
                .filter(Reservation::isExpired)
                .filter(r -> r.getStatus() == ReservationStatus.ACTIVE)
                .toList();

        for (Reservation reservation : expiredReservations) {
            reservation.setStatus(ReservationStatus.EXPIRED);
            Logger.logInfo("Reservation expired: " + reservation.getReservationId());
            
            // Notify next patron in queue if any
            List<Reservation> queuedReservations = getReservationsByBook(reservation.getIsbn());
            if (!queuedReservations.isEmpty()) {
                Reservation nextReservation = queuedReservations.get(0);
                Optional<Patron> nextPatronOpt = patronService.findPatronById(nextReservation.getPatronId());
                Optional<Book> bookOpt = bookService.findBookByIsbn(reservation.getIsbn());
                
                if (nextPatronOpt.isPresent() && bookOpt.isPresent()) {
                    notifyObservers("Book '" + bookOpt.get().getTitle() + 
                                   "' is now available for " + nextPatronOpt.get().getName());
                }
            }
        }
    }

    public void notifyBookAvailable(String isbn) {
        List<Reservation> queuedReservations = getReservationsByBook(isbn);
        if (!queuedReservations.isEmpty()) {
            Reservation nextReservation = queuedReservations.get(0);
            nextReservation.setStatus(ReservationStatus.FULFILLED);
            
            Optional<Patron> patronOpt = patronService.findPatronById(nextReservation.getPatronId());
            Optional<Book> bookOpt = bookService.findBookByIsbn(isbn);
            
            if (patronOpt.isPresent() && bookOpt.isPresent()) {
                notifyObservers("Reserved book '" + bookOpt.get().getTitle() + 
                               "' is now available for pickup by " + patronOpt.get().getName());
            }
        }
    }
}
