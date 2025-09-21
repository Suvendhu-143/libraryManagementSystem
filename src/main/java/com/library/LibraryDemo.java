package com.library;

import com.library.models.*;
import com.library.utils.Logger;
import java.util.List;
import java.util.Optional;

/**
 * Demonstration class to showcase the Library Management System functionality.
 */
public class LibraryDemo {
    public static void main(String[] args) {
        Logger.logInfo("Starting Library Management System Demo");
        
        LibraryManagementSystem library = new LibraryManagementSystem();
        
        // Demonstrate book management
        demonstrateBookManagement(library);
        
        // Demonstrate patron management
        demonstratePatronManagement(library);
        
        // Demonstrate lending process
        demonstrateLendingProcess(library);
        
        // Demonstrate reservation system
        demonstrateReservationSystem(library);
        
        // Generate final report
        library.generateInventoryReport();
        
        Logger.logInfo("Library Management System Demo completed");
    }
    
    private static void demonstrateBookManagement(LibraryManagementSystem library) {
        Logger.logInfo("\n=== BOOK MANAGEMENT DEMONSTRATION ===");
        
        // Add books
        library.addBook("978-0134685991", "Effective Java", "Joshua Bloch", 2017);
        library.addBook("978-0596009205", "Head First Design Patterns", "Eric Freeman", 2004);
        library.addBook("978-0132350884", "Clean Code", "Robert Martin", 2008);
        library.addBook("978-0201633610", "Design Patterns", "Gang of Four", 1994);
        library.addBook("978-0321356680", "Effective C++", "Scott Meyers", 2005);
        
        // Search books
        Logger.logInfo("\nSearching books by title 'Java':");
        List<Book> javaBooks = library.searchBooks("title", "Java");
        javaBooks.forEach(book -> Logger.logInfo("Found: " + book.getTitle()));
        
        Logger.logInfo("\nSearching books by author 'Martin':");
        List<Book> martinBooks = library.searchBooks("author", "Martin");
        martinBooks.forEach(book -> Logger.logInfo("Found: " + book.getTitle() + " by " + book.getAuthor()));
        
        // Display all books
        Logger.logInfo("\nAll books in library:");
        library.getAllBooks().forEach(book -> Logger.logInfo(book.toString()));
    }
    
    private static void demonstratePatronManagement(LibraryManagementSystem library) {
        Logger.logInfo("\n=== PATRON MANAGEMENT DEMONSTRATION ===");
        
        // Add patrons
        library.addPatron("STU001", "Alice Johnson", "alice@university.edu", "555-1234", "123 Campus Dr", PatronType.STUDENT);
        library.addPatron("FAC001", "Dr. Bob Smith", "bob@university.edu", "555-5678", "456 Faculty St", PatronType.FACULTY);
        library.addPatron("STF001", "Carol Brown", "carol@university.edu", "555-9012", "789 Staff Ave", PatronType.STAFF);
        library.addPatron("GEN001", "David Wilson", "david@email.com", "555-3456", "321 Public Rd", PatronType.GENERAL);
        
        // Search patrons
        Logger.logInfo("\nSearching patrons by name 'Smith':");
        List<Patron> smithPatrons = library.searchPatronsByName("Smith");
        smithPatrons.forEach(patron -> Logger.logInfo("Found: " + patron.getName() + " (" + patron.getPatronType() + ")"));
        
        // Display all patrons
        Logger.logInfo("\nAll patrons in library:");
        library.getAllPatrons().forEach(patron -> Logger.logInfo(patron.toString()));
    }
    
    private static void demonstrateLendingProcess(LibraryManagementSystem library) {
        Logger.logInfo("\n=== LENDING PROCESS DEMONSTRATION ===");
        
        try {
            // Borrow books
            Logger.logInfo("\nBorrowing books:");
            BorrowRecord record1 = library.borrowBook("STU001", "978-0134685991");
            Logger.logInfo("Borrow record created: " + record1.getRecordId());
            
            BorrowRecord record2 = library.borrowBook("FAC001", "978-0596009205");
            Logger.logInfo("Borrow record created: " + record2.getRecordId());
            
            BorrowRecord record3 = library.borrowBook("STF001", "978-0132350884");
            Logger.logInfo("Borrow record created: " + record3.getRecordId());
            
            // Try to borrow an already borrowed book (should fail)
            try {
                library.borrowBook("GEN001", "978-0134685991");
            } catch (IllegalStateException e) {
                Logger.logWarning("Expected error: " + e.getMessage());
            }
            
            // Check current borrowed books
            Logger.logInfo("\nCurrent borrowed books for Alice (STU001):");
            List<BorrowRecord> aliceBorrowedBooks = library.getCurrentBorrowedBooks("STU001");
            aliceBorrowedBooks.forEach(record -> {
                Optional<Book> book = library.findBook(record.getIsbn());
                book.ifPresent(b -> Logger.logInfo("Borrowed: " + b.getTitle()));
            });
            
            // Return a book
            Logger.logInfo("\nReturning book:");
            boolean returned = library.returnBook("STU001", "978-0134685991");
            Logger.logInfo("Book return status: " + returned);
            
        } catch (Exception e) {
            Logger.logError("Error in lending process: " + e.getMessage());
        }
    }
    
    private static void demonstrateReservationSystem(LibraryManagementSystem library) {
        Logger.logInfo("\n=== RESERVATION SYSTEM DEMONSTRATION ===");
        
        try {
            // Try to reserve an available book (should fail)
            try {
                library.reserveBook("GEN001", "978-0134685991");
            } catch (IllegalStateException e) {
                Logger.logWarning("Expected error: " + e.getMessage());
            }
            
            // Reserve a borrowed book
            Logger.logInfo("\nReserving borrowed books:");
            Reservation reservation1 = library.reserveBook("GEN001", "978-0596009205");
            Logger.logInfo("Reservation created: " + reservation1.getReservationId());
            
            Reservation reservation2 = library.reserveBook("STU001", "978-0596009205");
            Logger.logInfo("Reservation created: " + reservation2.getReservationId());
            
            // Check reservations for a book
            Logger.logInfo("\nReservations for Head First Design Patterns:");
            List<Reservation> bookReservations = library.getBookReservations("978-0596009205");
            bookReservations.forEach(res -> {
                Optional<Patron> patron = library.findPatron(res.getPatronId());
                patron.ifPresent(p -> Logger.logInfo("Reserved by: " + p.getName() + " on " + res.getReservationDate()));
            });
            
            // Return the book and trigger notification
            Logger.logInfo("\nReturning reserved book:");
            library.returnBook("FAC001", "978-0596009205");
            
        } catch (Exception e) {
            Logger.logError("Error in reservation system: " + e.getMessage());
        }
    }
}
