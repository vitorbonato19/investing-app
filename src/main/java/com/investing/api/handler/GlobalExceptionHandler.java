package com.investing.api.handler;

import com.investing.api.exceptions.RegisterNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(RegisterNotFoundException.class)
    public ResponseEntity<Map<String, Object>> registerNotFoundException(RegisterNotFoundException ex, HttpServletRequest request) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", ex.getStatus().value());
        response.put("statusCode", ex.getStatus());
        response.put("details", ex.getMessage());

        return new ResponseEntity<>(response, ex.getStatus());
    }
}
