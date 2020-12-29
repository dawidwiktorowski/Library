package com.dawid.library.exception;

public class UserAlReadyExistsException extends RuntimeException {
    public UserAlReadyExistsException(String message) {
        super(message);
    }
}
