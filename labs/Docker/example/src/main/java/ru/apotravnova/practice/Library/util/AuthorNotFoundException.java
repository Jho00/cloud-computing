package ru.apotravnova.practice.Library.util;

public class AuthorNotFoundException extends RuntimeException { //extend RunTime
    public AuthorNotFoundException() {
        super("The author did not found!");
    }
}
