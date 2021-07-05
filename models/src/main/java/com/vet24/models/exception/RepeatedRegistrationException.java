package com.vet24.models.exception;



public class RepeatedRegistrationException extends RuntimeException{

    public RepeatedRegistrationException(String message) {
        super(message);
    }
    public RepeatedRegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
