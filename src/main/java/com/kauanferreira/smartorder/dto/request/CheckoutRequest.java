package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;


/**
 * Request payload for the customer checkout flow.
 *
 * <p>Used by the "Buy now" action on the product detail page. The authenticated
 * user is resolved from the JWT token by the service layer, so no userId is
 * accepted in the body for security reasons.</p>
 *
 * @param productId the id of the product being purchased
 * @param quantity  the desired quantity (must be at least 1)
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
public record CheckoutRequest(

        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @Min(value = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
