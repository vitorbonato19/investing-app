package com.investing.api.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidRequestException extends RuntimeException {

    private final HttpStatus status;

    public InvalidRequestException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
