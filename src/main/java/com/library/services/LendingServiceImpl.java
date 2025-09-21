package com.library.services;

import com.library.models.*;
import com.library.patterns.Subject;
import com.library.utils.IdGenerator;
import com.library.utils.Logger;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of LendingService.
 * Extends Subject to implement Observer pattern for notifications.
 */
public class LendingServiceImpl extends Subject implements LendingService {
    private final BookService bookService;
    private final PatronService patronService;
    private final Map<String, BorrowRecord> borrowRecords;
    private static final double FINE_PER_DAY = 1.0; // $1 per day
    private static final int STANDARD_LOAN_PERIOD = 14; // 14 days

    public LendingServiceImpl(BookService bookService, PatronService patronService) {
        this.bookService = bookService;
        this.patronService = patronService;
        this.borrowRecords = new ConcurrentHashMap<>();
    }

    @Override
    public BorrowRecord borrowBook(String patronId, String isbn) {
        if (patronId == null || isbn == null) {
            throw new IllegalArgumentException("Patron ID and ISBN cannot be null");
        }

        // Find patron and book
        Optional<Patron> patronOpt = patronService.findPatronById(patronId);
        Optional<Book> bookOpt = bookService.findBookByIsbn(isbn);

        if (patronOpt.isEmpty()) {
            throw new IllegalArgumentException("Patron not found: " + patronId);
        }

        if (bookOpt.isEmpty()) {
            throw new IllegalArgumentException("Book not found: " + isbn);
        }

        Patron patron = patronOpt.get();
        Book book = bookOpt.get();

        // Validate borrowing conditions
        if (!patron.canBorrowBook()) {
            throw new IllegalStateException("Patron cannot borrow more books");
        }

        if (!book.isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }

        // Create borrow record
        LocalDate borrowDate = LocalDate.now();
        LocalDate dueDate = borrowDate.plusDays(STANDARD_LOAN_PERIOD);
        String recordId = IdGenerator.generateRecordId();

        BorrowRecord record = new BorrowRecord(recordId, patronId, isbn, borrowDate, dueDate);
        borrowRecords.put(recordId, record);

        // Update book and patron
        book.borrowBook(patronId, borrowDate, dueDate);
        patron.addBorrowRecord(record);

        Logger.logInfo("Book borrowed: " + book.getTitle() + " by " + patron.getName());
        notifyObservers("Book '" + book.getTitle() + "' has been borrowed by " + patron.getName());

        return record;
    }

    @Override
    public boolean returnBook(String patronId, String isbn) {
        if (patronId == null || isbn == null) {
            throw new IllegalArgumentException("Patron ID and ISBN cannot be null");
        }

        // Find the active borrow record
        Optional<BorrowRecord> recordOpt = borrowRecords.values().stream()
                .filter(record -> record.getPatronId().equals(patronId) && 
                                 record.getIsbn().equals(isbn) && 
                                 record.getReturnDate() == null)
                .findFirst();

        if (recordOpt.isEmpty()) {
            Logger.logWarning("No active borrow record found for patron: " + patronId + " and book: " + isbn);
            return false;
        }

        BorrowRecord record = recordOpt.get();
        Optional<Book> bookOpt = bookService.findBookByIsbn(isbn);
        Optional<Patron> patronOpt = patronService.findPatronById(patronId);

        if (bookOpt.isEmpty() || patronOpt.isEmpty()) {
            return false;
        }

        Book book = bookOpt.get();
        Patron patron = patronOpt.get();

        // Process return
        LocalDate returnDate = LocalDate.now();
        record.setReturnDate(returnDate);

        // Calculate fine if overdue
        if (record.isOverdue()) {
            double fine = calculateFine(record);
            record.setFineAmount(fine);
            Logger.logWarning("Book returned late. Fine: $" + fine);
        }

        book.returnBook();
        Logger.logInfo("Book returned: " + book.getTitle() + " by " + patron.getName());
        notifyObservers("Book '" + book.getTitle() + "' has been returned by " + patron.getName());

        return true;
    }

    @Override
    public List<BorrowRecord> getBorrowingHistory(String patronId) {
        return borrowRecords.values().stream()
                .filter(record -> record.getPatronId().equals(patronId))
                .toList();
    }

    @Override
    public List<BorrowRecord> getOverdueBooks() {
        return borrowRecords.values().stream()
                .filter(BorrowRecord::isOverdue)
                .toList();
    }

    @Override
    public List<BorrowRecord> getCurrentBorrowedBooks(String patronId) {
        return borrowRecords.values().stream()
                .filter(record -> record.getPatronId().equals(patronId) && 
                                 record.getReturnDate() == null)
                .toList();
    }

    @Override
    public double calculateFine(BorrowRecord record) {
        if (!record.isOverdue()) {
            return 0.0;
        }
        
        long daysOverdue = record.getDaysOverdue();
        return daysOverdue * FINE_PER_DAY;
    }
}
