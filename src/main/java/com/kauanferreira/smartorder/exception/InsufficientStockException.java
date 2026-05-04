package com.kauanferreira.smartorder.exception;

/**
 * Exception thrown when attempting to add or update a cart item
 * with a quantity that exceeds the available product stock.
 *
 * <p>This exception is also thrown when attempting to add a product
 * that is currently out of stock (stockQuantity is null or zero).
 * Maps to HTTP 409 (Conflict) in the REST layer.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
