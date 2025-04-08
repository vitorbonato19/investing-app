package com.investing.api.exceptions;

import org.springframework.http.HttpStatus;

public class BadLoginRequestException extends RuntimeException {

    private final HttpStatus status;

    public BadLoginRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
