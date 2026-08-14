package com.financialtransaction.exception;

public class NoTransactionAvailableException extends RuntimeException {

    public NoTransactionAvailableException(String message) {
        super(message);
    }
}
