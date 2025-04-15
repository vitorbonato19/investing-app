package com.investing.api.exceptions;

import org.springframework.http.HttpStatus;

public class StockNotFoundException extends RuntimeException {

    private final HttpStatus httpStatus;

    public StockNotFoundException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getStatus() {
        return httpStatus;
    }
}
