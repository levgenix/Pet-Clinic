package com.vet24.models.exception;

import java.io.IOException;

public class CredentialException extends IOException {

    private String mail;

    public String getMail() {
        return mail;
    }

    public CredentialException(String message, String mail) {
        super(message);
        this.mail = mail;
    }
}
