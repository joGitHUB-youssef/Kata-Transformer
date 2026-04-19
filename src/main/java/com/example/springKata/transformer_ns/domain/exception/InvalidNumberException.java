package com.example.springKata.transformer_ns.domain.exception;

public class InvalidNumberException extends RuntimeException {

    public InvalidNumberException(int number) {
        super("Number must be between 0 and 100, got: " + number);
    }
}
