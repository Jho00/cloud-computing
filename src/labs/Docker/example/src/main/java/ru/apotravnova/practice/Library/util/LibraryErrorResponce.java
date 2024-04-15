package ru.apotravnova.practice.Library.util;

public class LibraryErrorResponce {
    private String message;
    private long timestamp;

    public LibraryErrorResponce(String message, long timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
