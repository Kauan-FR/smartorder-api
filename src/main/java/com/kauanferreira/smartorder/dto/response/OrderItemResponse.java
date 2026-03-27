package com.kauanferreira.smartorder.dto.response;

import java.math.BigDecimal;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.OrderItem} data to the client.
 *
 * <p>Contains the item details along with its associated product
 * represented as a nested {@link ProductResponse}.</p>
 *
 * @param id       the order item unique identifier
 * @param quantity the item quantity
 * @param price    the unit price at purchase time
 * @param subtotal the line total
 * @param product  the associated product data
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record OrderItemResponse(

        Long id,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal,
        Long orderId,
        ProductResponse product
) {
}
