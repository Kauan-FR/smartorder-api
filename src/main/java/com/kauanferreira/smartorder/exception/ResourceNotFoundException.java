package com.kauanferreira.smartorder.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 *
 * <p>This exception replaces {@link jakarta.persistence.EntityNotFoundException}
 * to decouple business logic from framework-specific exceptions.
 * Maps to HTTP 404 (Not Found) in the REST layer.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public class ResourceNotFoundException extends RuntimeException {
    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message the detail message describing which resource was not found
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
