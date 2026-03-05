package com.kauanferreira.smartorder.exception;

/**
 * Exception thrown when a database operation fails unexpectedly.
 *
 * <p>Encapsulates low-level database errors to prevent infrastructure
 * details from leaking to upper layers.
 * Maps to HTTP 500 (Internal Server Error) in the REST layer.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public class DatabaseOperationException extends RuntimeException {

    /**
     * Constructs a new DatabaseOperationException with the specified detail message.
     *
     * @param message the detail message describing the database failure
     */
    public DatabaseOperationException(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseOperationException with the specified detail message
     * and cause.
     *
     * <p>The cause parameter preserves the original database exception
     * for logging and debugging purposes.</p>
     *
     * @param message the detail message describing the database failure
     * @param cause the original exception that caused this error
     */
    public DatabaseOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
