package com.example.lab2projekt.domain.Exceptions;

public class RESTPizzaNotFoundException extends RuntimeException{
    public RESTPizzaNotFoundException(String message) {
        super(message);
    }
}
