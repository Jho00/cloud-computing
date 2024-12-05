package ru.apotravnova.practice.Library.util;

public class PatnetNotFoundException extends RuntimeException {

    public PatnetNotFoundException() {
        super("The patent did not found!");
    }
}
