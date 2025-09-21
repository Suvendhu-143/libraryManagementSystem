package com.library.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a book in the library management system.
 * Demonstrates encapsulation by keeping fields private and providing controlled access through methods.
 */
public class Book {
    private String isbn;
    private String title;
    private String author;
    private int publicationYear;
    private BookStatus status;
    private String borrowerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    // Constructor
    public Book(String isbn, String title, String author, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
        this.status = BookStatus.AVAILABLE;
    }

    // Getters
    public String getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public BookStatus getStatus() {
        return status;
    }

    public String getBorrowerId() {
        return borrowerId;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public void setStatus(BookStatus status) {
        this.status = status;
    }

    public void setBorrowerId(String borrowerId) {
        this.borrowerId = borrowerId;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Business methods
    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    public void borrowBook(String patronId, LocalDate borrowDate, LocalDate dueDate) {
        if (!isAvailable()) {
            throw new IllegalStateException("Book is not available for borrowing");
        }
        this.borrowerId = patronId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = BookStatus.BORROWED;
    }

    public void returnBook() {
        this.borrowerId = null;
        this.borrowDate = null;
        this.dueDate = null;
        this.status = BookStatus.AVAILABLE;
    }

    public boolean isOverdue() {
        return status == BookStatus.BORROWED && dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }

    @Override
    public String toString() {
        return "Book{" +
                "isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publicationYear=" + publicationYear +
                ", status=" + status +
                '}';
    }
}
