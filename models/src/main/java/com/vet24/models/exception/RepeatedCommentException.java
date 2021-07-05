package com.vet24.models.exception;

public class RepeatedCommentException extends RuntimeException {

    public RepeatedCommentException(String message) {
        super(message);
    }

    public RepeatedCommentException(String message, Throwable cause) {
        super(message, cause);
    }

    public RepeatedCommentException(Throwable cause) {
        super(cause);
    }
}
