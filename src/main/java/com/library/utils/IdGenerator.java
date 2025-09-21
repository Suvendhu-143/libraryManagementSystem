package com.library.utils;

import java.util.UUID;

/**
 * Utility class for generating unique IDs.
 */
public class IdGenerator {
    
    public static String generateBookId() {
        return "BOOK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static String generatePatronId() {
        return "PATRON-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static String generateRecordId() {
        return "RECORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    public static String generateReservationId() {
        return "RESERVATION-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}
