package com.investing.api.exceptions;

import org.springframework.http.HttpStatus;

public class RegisterNotFoundException extends RuntimeException {

    private HttpStatus status;

    public RegisterNotFoundException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
