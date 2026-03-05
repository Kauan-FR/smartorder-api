package com.kauanferreira.smartorder.exception;

/**
 * Exception thrown when attempting to create or update a resource
 * that would violate a uniqueness constraint.
 *
 * <p>This exception replaces {@link IllegalArgumentException} for duplicate
 * scenarios such as duplicate emails or names.
 * Maps to HTTP 409 (Conflict) in the REST layer.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public class DuplicateResourceException extends RuntimeException {

    /**
     * Constructs a new DuplicateResourceException with the specified detail message.
     *
     * @param message the detail message describing which resource is duplicated
     */
    public DuplicateResourceException(String message) {
        super(message);
    }
}
