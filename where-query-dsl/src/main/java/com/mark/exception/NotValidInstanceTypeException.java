package com.mark.exception;

public class NotValidInstanceTypeException extends RuntimeException {
    public NotValidInstanceTypeException(String message) {
        super(message);
    }
    public NotValidInstanceTypeException(String message, Throwable cause) {
        super(message, cause);
    }
}
