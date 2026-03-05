package com.kauanferreira.smartorder.exception;

/**
 * Exception thrown when a business rule is violated during an operation.
 *
 * <p>Used for domain-specific validations such as ordering inactive products,
 * insufficient stock, or invalid order status transitions.
 * Maps to HTTP 422 (Unprocessable Entity) in the REST layer.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public class BusinessRuleException extends RuntimeException {

    /**
     * Constructs a new BusinessRuleException with the specified detail message.
     *
     * @param message the detail message describing which business rule was violated
     */
    public BusinessRuleException(String message) {
        super(message);
    }
}
