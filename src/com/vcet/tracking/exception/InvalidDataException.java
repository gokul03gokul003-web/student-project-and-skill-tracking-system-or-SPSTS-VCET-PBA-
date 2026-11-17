package com.vcet.tracking.exception;

/**
 * Custom Exception thrown when input data violates domain or validation constraints.
 */
public class InvalidDataException extends Exception {
    private static final long serialVersionUID = 1L;

    public InvalidDataException(String message) {
        super(message);
    }
}
