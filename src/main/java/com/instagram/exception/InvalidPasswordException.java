package com.instagram.exception;

public class InvalidPasswordException extends RuntimeException {

    public InvalidPasswordException(String message) {
        super(message);
    }
}
