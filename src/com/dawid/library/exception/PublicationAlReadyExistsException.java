package com.dawid.library.exception;

public class PublicationAlReadyExistsException extends RuntimeException{
    public PublicationAlReadyExistsException(String message) {
        super(message);
    }
}
