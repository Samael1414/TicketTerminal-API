package com.ticket.terminal.exception;

public class EmptyRefundListException extends RuntimeException {

    public EmptyRefundListException(String message) {
        super(message);
    }
}
