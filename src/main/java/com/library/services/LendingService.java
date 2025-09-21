package com.library.services;

import com.library.models.BorrowRecord;
import java.util.List;

/**
 * Interface for lending operations.
 * Follows the Single Responsibility Principle by focusing only on lending operations.
 */
public interface LendingService {
    BorrowRecord borrowBook(String patronId, String isbn);
    boolean returnBook(String patronId, String isbn);
    List<BorrowRecord> getBorrowingHistory(String patronId);
    List<BorrowRecord> getOverdueBooks();
    List<BorrowRecord> getCurrentBorrowedBooks(String patronId);
    double calculateFine(BorrowRecord record);
}
