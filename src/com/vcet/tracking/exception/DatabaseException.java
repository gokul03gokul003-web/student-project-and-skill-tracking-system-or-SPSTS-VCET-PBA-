package com.vcet.tracking.exception;

/**
 * Custom Exception for database connectivity and query execution errors.
 * Demonstrates Unit 3: Exception Handling, User-defined Exceptions.
 */
public class DatabaseException extends Exception {
    private static final long serialVersionUID = 1L;

    public DatabaseException(String message) {
        super(message);
    }

    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
