package com.library;

import com.library.models.*;
import com.library.services.*;
import com.library.patterns.EmailNotificationObserver;
import com.library.utils.Logger;
import java.util.List;
import java.util.Optional;

/**
 * Main Library Management System class that coordinates all services.
 * Demonstrates the Facade pattern by providing a simplified interface to the complex subsystem.
 */
public class LibraryManagementSystem {
    private final BookService bookService;
    private final PatronService patronService;
    private final LendingService lendingService;
    private final ReservationService reservationService;

    public LibraryManagementSystem() {
        this.bookService = new BookServiceImpl();
        this.patronService = new PatronServiceImpl();
        this.lendingService = new LendingServiceImpl(bookService, patronService);
        this.reservationService = new ReservationServiceImpl(bookService, patronService);
        
        // Set up observer pattern for notifications
        setupNotifications();
    }

    private void setupNotifications() {
        // Add email notification observers
        if (lendingService instanceof LendingServiceImpl) {
            LendingServiceImpl lendingImpl = (LendingServiceImpl) lendingService;
            lendingImpl.addObserver(new EmailNotificationObserver("librarian@library.com"));
        }
        
        if (reservationService instanceof ReservationServiceImpl) {
            ReservationServiceImpl reservationImpl = (ReservationServiceImpl) reservationService;
            reservationImpl.addObserver(new EmailNotificationObserver("librarian@library.com"));
        }
    }

    // Book Management Methods
    public void addBook(String isbn, String title, String author, int publicationYear) {
        Book book = new Book(isbn, title, author, publicationYear);
        bookService.addBook(book);
        Logger.logInfo("Book added to library system: " + title);
    }

    public void removeBook(String isbn) {
        bookService.removeBook(isbn);
    }

    public List<Book> searchBooks(String searchType, String searchTerm) {
        return switch (searchType.toLowerCase()) {
            case "title" -> bookService.searchBooksByTitle(searchTerm);
            case "author" -> bookService.searchBooksByAuthor(searchTerm);
            case "isbn" -> bookService.searchBooksByIsbn(searchTerm);
            default -> throw new IllegalArgumentException("Invalid search type: " + searchType);
        };
    }

    public Optional<Book> findBook(String isbn) {
        return bookService.findBookByIsbn(isbn);
    }

    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    public List<Book> getAvailableBooks() {
        return bookService.getAvailableBooks();
    }

    // Patron Management Methods
    public void addPatron(String patronId, String name, String email, String phoneNumber, 
                         String address, PatronType patronType) {
        Patron patron = new Patron(patronId, name, email, phoneNumber, address, patronType);
        patronService.addPatron(patron);
        
        // Add email notification observer for this patron
        if (lendingService instanceof LendingServiceImpl) {
            LendingServiceImpl lendingImpl = (LendingServiceImpl) lendingService;
            lendingImpl.addObserver(new EmailNotificationObserver(email));
        }
        
        Logger.logInfo("Patron added to library system: " + name);
    }

    public void removePatron(String patronId) {
        patronService.removePatron(patronId);
    }

    public Optional<Patron> findPatron(String patronId) {
        return patronService.findPatronById(patronId);
    }

    public List<Patron> getAllPatrons() {
        return patronService.getAllPatrons();
    }

    public List<Patron> searchPatronsByName(String name) {
        return patronService.searchPatronsByName(name);
    }

    // Lending Methods
    public BorrowRecord borrowBook(String patronId, String isbn) {
        try {
            BorrowRecord record = lendingService.borrowBook(patronId, isbn);
            Logger.logInfo("Book successfully borrowed");
            return record;
        } catch (Exception e) {
            Logger.logError("Failed to borrow book: " + e.getMessage());
            throw e;
        }
    }

    public boolean returnBook(String patronId, String isbn) {
        try {
            boolean success = lendingService.returnBook(patronId, isbn);
            if (success) {
                Logger.logInfo("Book successfully returned");
                
                // Notify next patron in reservation queue if any
                if (reservationService instanceof ReservationServiceImpl) {
                    ReservationServiceImpl reservationImpl = (ReservationServiceImpl) reservationService;
                    reservationImpl.notifyBookAvailable(isbn);
                }
            }
            return success;
        } catch (Exception e) {
            Logger.logError("Failed to return book: " + e.getMessage());
            throw e;
        }
    }

    public List<BorrowRecord> getPatronBorrowingHistory(String patronId) {
        return lendingService.getBorrowingHistory(patronId);
    }

    public List<BorrowRecord> getOverdueBooks() {
        return lendingService.getOverdueBooks();
    }

    public List<BorrowRecord> getCurrentBorrowedBooks(String patronId) {
        return lendingService.getCurrentBorrowedBooks(patronId);
    }

    // Reservation Methods
    public Reservation reserveBook(String patronId, String isbn) {
        try {
            Reservation reservation = reservationService.reserveBook(patronId, isbn);
            Logger.logInfo("Book successfully reserved");
            return reservation;
        } catch (Exception e) {
            Logger.logError("Failed to reserve book: " + e.getMessage());
            throw e;
        }
    }

    public boolean cancelReservation(String reservationId) {
        return reservationService.cancelReservation(reservationId);
    }

    public List<Reservation> getPatronReservations(String patronId) {
        return reservationService.getReservationsByPatron(patronId);
    }

    public List<Reservation> getBookReservations(String isbn) {
        return reservationService.getReservationsByBook(isbn);
    }

    // Utility Methods
    public void processExpiredReservations() {
        reservationService.processExpiredReservations();
    }

    public void generateInventoryReport() {
        List<Book> allBooks = bookService.getAllBooks();
        List<Book> availableBooks = bookService.getAvailableBooks();
        List<Book> borrowedBooks = bookService.getBorrowedBooks();
        List<BorrowRecord> overdueBooks = lendingService.getOverdueBooks();

        Logger.logInfo("=== LIBRARY INVENTORY REPORT ===");
        Logger.logInfo("Total Books: " + allBooks.size());
        Logger.logInfo("Available Books: " + availableBooks.size());
        Logger.logInfo("Borrowed Books: " + borrowedBooks.size());
        Logger.logInfo("Overdue Books: " + overdueBooks.size());
        Logger.logInfo("==============================");
    }

    // Getters for services (useful for testing or advanced operations)
    public BookService getBookService() {
        return bookService;
    }

    public PatronService getPatronService() {
        return patronService;
    }

    public LendingService getLendingService() {
        return lendingService;
    }

    public ReservationService getReservationService() {
        return reservationService;
    }
}
