package com.ticket.terminal.exception;

public class InvalidRefundRequestException extends RuntimeException {

    public InvalidRefundRequestException(String message) {
        super(message);
    }
}
