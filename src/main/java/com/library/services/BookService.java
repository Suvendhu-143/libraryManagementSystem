package com.library.services;

import com.library.models.Book;
import java.util.List;
import java.util.Optional;

/**
 * Interface for book management operations.
 * Follows the Interface Segregation Principle by focusing only on book-related operations.
 */
public interface BookService {
    void addBook(Book book);
    void removeBook(String isbn);
    Optional<Book> updateBook(String isbn, Book updatedBook);
    Optional<Book> findBookByIsbn(String isbn);
    List<Book> searchBooksByTitle(String title);
    List<Book> searchBooksByAuthor(String author);
    List<Book> searchBooksByIsbn(String isbn);
    List<Book> getAllBooks();
    List<Book> getAvailableBooks();
    List<Book> getBorrowedBooks();
}
