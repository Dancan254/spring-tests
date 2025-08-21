package com.javaguy.testing.exception;

public class SchoolAlreadyExistsException extends RuntimeException {
    public SchoolAlreadyExistsException(String message) {
        super(message);
    }
}
