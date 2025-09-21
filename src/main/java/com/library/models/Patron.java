package com.library.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a library patron (member).
 * Demonstrates encapsulation and maintains patron information and borrowing history.
 */
public class Patron {
    private String patronId;
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private PatronType patronType;
    private LocalDate membershipDate;
    private PatronStatus status;
    private List<BorrowRecord> borrowingHistory;
    private int maxBooksAllowed;

    public Patron(String patronId, String name, String email, String phoneNumber, String address, PatronType patronType) {
        this.patronId = patronId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.patronType = patronType;
        this.membershipDate = LocalDate.now();
        this.status = PatronStatus.ACTIVE;
        this.borrowingHistory = new ArrayList<>();
        this.maxBooksAllowed = patronType.getMaxBooksAllowed();
    }

    // Getters
    public String getPatronId() {
        return patronId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public PatronType getPatronType() {
        return patronType;
    }

    public LocalDate getMembershipDate() {
        return membershipDate;
    }

    public PatronStatus getStatus() {
        return status;
    }

    public List<BorrowRecord> getBorrowingHistory() {
        return new ArrayList<>(borrowingHistory);
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(PatronStatus status) {
        this.status = status;
    }

    public void setPatronType(PatronType patronType) {
        this.patronType = patronType;
        this.maxBooksAllowed = patronType.getMaxBooksAllowed();
    }

    // Business methods
    public boolean canBorrowBook() {
        return status == PatronStatus.ACTIVE && getCurrentBorrowedBooksCount() < maxBooksAllowed;
    }

    public int getCurrentBorrowedBooksCount() {
        return (int) borrowingHistory.stream()
                .filter(record -> record.getReturnDate() == null)
                .count();
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowingHistory.add(record);
    }

    public List<BorrowRecord> getCurrentBorrowedBooks() {
        return borrowingHistory.stream()
                .filter(record -> record.getReturnDate() == null)
                .toList();
    }

    public List<BorrowRecord> getOverdueBooks() {
        return borrowingHistory.stream()
                .filter(record -> record.getReturnDate() == null && record.isOverdue())
                .toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronId, patron.patronId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patronId);
    }

    @Override
    public String toString() {
        return "Patron{" +
                "patronId='" + patronId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", patronType=" + patronType +
                ", status=" + status +
                '}';
    }
}
