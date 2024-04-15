package ru.apotravnova.practice.Library.util;

public class AuthorCreatedException extends RuntimeException{
    public AuthorCreatedException(String message) {
        //вызываем конструктор наследуемого класса RuntimeException
        super(message);
    }
}
