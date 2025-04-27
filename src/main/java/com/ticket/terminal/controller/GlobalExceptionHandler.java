package com.ticket.terminal.controller;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public String handleUnsupportedMediaType(HttpMediaTypeNotSupportedException ex) {
        System.out.println("==== UNSUPPORTED MEDIA TYPE ====");
        System.out.println("Provided Content-Type: " + ex.getContentType());
        System.out.println("Supported Media Types: " + ex.getSupportedMediaTypes());
        return "Unsupported Media Type: " + ex.getContentType();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public String handleBadBody(HttpMessageNotReadableException ex) {
        ex.printStackTrace();
        return "Invalid JSON format: " + ex.getMessage();
    }
}
