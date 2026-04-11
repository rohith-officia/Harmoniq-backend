package com.harmoniq.backend.common.exception;

public class ArtistProfileAlreadyExistsException extends RuntimeException {
    public ArtistProfileAlreadyExistsException(String message) {
        super(message);
    }
}