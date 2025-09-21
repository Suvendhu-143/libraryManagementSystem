package com.library.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Simple logging utility for the library management system.
 */
public class Logger {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.out.println("[" + timestamp + "] " + message);
    }

    public static void logError(String message) {
        String timestamp = LocalDateTime.now().format(formatter);
        System.err.println("[" + timestamp + "] ERROR: " + message);
    }

    public static void logInfo(String message) {
        log("INFO: " + message);
    }

    public static void logWarning(String message) {
        log("WARNING: " + message);
    }
}
