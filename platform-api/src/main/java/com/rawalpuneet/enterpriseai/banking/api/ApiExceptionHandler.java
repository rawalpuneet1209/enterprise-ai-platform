package com.rawalpuneet.enterpriseai.banking.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(IllegalArgumentException.class) @ResponseStatus(HttpStatus.BAD_REQUEST)
    ErrorResponse invalidRequest(IllegalArgumentException exception) { return new ErrorResponse("INVALID_REQUEST", exception.getMessage(), Instant.now()); }
    record ErrorResponse(String code, String message, Instant timestamp) { }
}

