package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.validation.PositiveOrZeroInteger;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for adding or updating a {@link com.kauanferreira.smartorder.entity.CartItem}.
 *
 * <p>The user is not included because the backend extracts it
 * from the authenticated JWT token.</p>
 *
 * @param productId the id of the product to add to the cart (required)
 * @param quantity  the desired quantity (required, minimum 1)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record CartItemRequest(

        @NotNull(message = "Product id is required")
        Long productId,

        @NotNull(message = "Quantity is required")
        @PositiveOrZeroInteger(fieldName = "Cart item quantity", minValue = 1, message = "Quantity must be at least 1")
        Integer quantity
) {
}
