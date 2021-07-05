package com.vet24.models.exception;

public class NoSuchAbstractEntityDtoException extends RuntimeException{
    public NoSuchAbstractEntityDtoException() {
    }

    public NoSuchAbstractEntityDtoException(String message) {
        super(message);
    }

    public NoSuchAbstractEntityDtoException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoSuchAbstractEntityDtoException(Throwable cause) {
        super(cause);
    }
}
