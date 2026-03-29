package com.kauanferreira.smartorder.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.CartItem} data to the client.
 *
 * <p>Contains the cart item details with the full product information
 * represented as a nested {@link ProductResponse}.</p>
 *
 * @param id       the cart item unique identifier
 * @param product  the product data in the cart
 * @param quantity the quantity of the product
 * @param addedAt  the timestamp when the item was added to the cart
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record CartItemResponse(
        Long id,
        ProductResponse product,
        Integer quantity,
        LocalDateTime addedAt
) {
}
