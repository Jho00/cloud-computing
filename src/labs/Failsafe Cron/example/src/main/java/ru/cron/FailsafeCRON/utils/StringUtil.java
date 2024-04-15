package ru.cron.FailsafeCRON.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtil {
    public static void printNumberOfEntriesBefore(Integer numberOfEntries) {
        System.out.println("Number of entries in database before script: " + numberOfEntries);
    }

    public static void printNumberOfEntriesAfter(Integer numberOfEntries) {
        System.out.println("Number of entries in database after script: " + numberOfEntries);
    }

    public static void printLock() {
        System.out.println("\nLOCK\n");
    }

    public static void printSkip() {
        System.out.println("\nSKIP\n");
    }
}
