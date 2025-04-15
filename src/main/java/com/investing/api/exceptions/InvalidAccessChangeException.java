package com.investing.api.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAccessChangeException extends RuntimeException {

    private final HttpStatus status;

    public InvalidAccessChangeException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
