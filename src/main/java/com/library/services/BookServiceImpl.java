package com.library.services;

import com.library.models.Book;
import com.library.patterns.*;
import com.library.utils.Logger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementation of BookService.
 * Demonstrates the use of Strategy pattern for search functionality.
 */
public class BookServiceImpl implements BookService {
    private final Map<String, Book> books;
    private final Map<String, SearchStrategy> searchStrategies;

    public BookServiceImpl() {
        this.books = new ConcurrentHashMap<>();
        this.searchStrategies = new HashMap<>();
        initializeSearchStrategies();
    }

    private void initializeSearchStrategies() {
        searchStrategies.put("title", new TitleSearchStrategy());
        searchStrategies.put("author", new AuthorSearchStrategy());
        searchStrategies.put("isbn", new ISBNSearchStrategy());
    }

    @Override
    public void addBook(Book book) {
        if (book == null || book.getIsbn() == null || book.getIsbn().trim().isEmpty()) {
            throw new IllegalArgumentException("Book and ISBN cannot be null or empty");
        }
        
        if (books.containsKey(book.getIsbn())) {
            throw new IllegalArgumentException("Book with ISBN " + book.getIsbn() + " already exists");
        }
        
        books.put(book.getIsbn(), book);
        Logger.logInfo("Book added: " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
    }

    @Override
    public void removeBook(String isbn) {
        if (isbn == null || isbn.trim().isEmpty()) {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
        
        Book removedBook = books.remove(isbn);
        if (removedBook != null) {
            Logger.logInfo("Book removed: " + removedBook.getTitle() + " (ISBN: " + isbn + ")");
        } else {
            Logger.logWarning("Attempted to remove non-existent book with ISBN: " + isbn);
        }
    }

    @Override
    public Optional<Book> updateBook(String isbn, Book updatedBook) {
        if (isbn == null || updatedBook == null) {
            throw new IllegalArgumentException("ISBN and updated book cannot be null");
        }
        
        Book existingBook = books.get(isbn);
        if (existingBook != null) {
            existingBook.setTitle(updatedBook.getTitle());
            existingBook.setAuthor(updatedBook.getAuthor());
            existingBook.setPublicationYear(updatedBook.getPublicationYear());
            Logger.logInfo("Book updated: " + existingBook.getTitle() + " (ISBN: " + isbn + ")");
            return Optional.of(existingBook);
        }
        
        Logger.logWarning("Attempted to update non-existent book with ISBN: " + isbn);
        return Optional.empty();
    }

    @Override
    public Optional<Book> findBookByIsbn(String isbn) {
        return Optional.ofNullable(books.get(isbn));
    }

    @Override
    public List<Book> searchBooksByTitle(String title) {
        SearchStrategy strategy = searchStrategies.get("title");
        return strategy.search(new ArrayList<>(books.values()), title);
    }

    @Override
    public List<Book> searchBooksByAuthor(String author) {
        SearchStrategy strategy = searchStrategies.get("author");
        return strategy.search(new ArrayList<>(books.values()), author);
    }

    @Override
    public List<Book> searchBooksByIsbn(String isbn) {
        SearchStrategy strategy = searchStrategies.get("isbn");
        return strategy.search(new ArrayList<>(books.values()), isbn);
    }

    @Override
    public List<Book> getAllBooks() {
        return new ArrayList<>(books.values());
    }

    @Override
    public List<Book> getAvailableBooks() {
        return books.values().stream()
                .filter(Book::isAvailable)
                .toList();
    }

    @Override
    public List<Book> getBorrowedBooks() {
        return books.values().stream()
                .filter(book -> !book.isAvailable())
                .toList();
    }
}
