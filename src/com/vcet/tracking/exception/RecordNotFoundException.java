package com.vcet.tracking.exception;

/**
 * Custom Exception thrown when an expected database record is not found.
 */
public class RecordNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public RecordNotFoundException(String message) {
        super(message);
    }
}
