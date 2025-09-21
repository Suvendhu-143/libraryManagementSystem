package com.library.models;

/**
 * Enum representing different types of library patrons with their privileges.
 */
public enum PatronType {
    STUDENT(3),
    FACULTY(10),
    STAFF(5),
    GENERAL(2);

    private final int maxBooksAllowed;

    PatronType(int maxBooksAllowed) {
        this.maxBooksAllowed = maxBooksAllowed;
    }

    public int getMaxBooksAllowed() {
        return maxBooksAllowed;
    }
}
