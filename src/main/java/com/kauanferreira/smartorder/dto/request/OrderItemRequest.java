package com.kauanferreira.smartorder.dto.request;


import com.kauanferreira.smartorder.validation.PositiveOrZeroDecimal;
import com.kauanferreira.smartorder.validation.PositiveOrZeroInteger;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO for creating and updating an {@link com.kauanferreira.smartorder.entity.OrderItem}.
 *
 * <p>The order and product are referenced by their ids rather than full entities.</p>
 *
 * @param quantity  the item quantity (required, must be at least 1)
 * @param price     the unit price at purchase time (required, must be zero or positive)
 * @param subtotal  the line total (required, must be zero or positive)
 * @param orderId   the id of the associated order (required)
 * @param productId the id of the associated product (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record OrderItemRequest(

        @PositiveOrZeroInteger(fieldName = "Order item quantity", minValue = 1)
        Integer quantity,

        @PositiveOrZeroDecimal(fieldName = "Order item price")
        BigDecimal price,

        @PositiveOrZeroDecimal(fieldName = "Order item subtotal")
        BigDecimal subtotal,

        @NotNull(message = "Order is required")
        Long orderId,

        @NotNull(message = "Product is required")
        Long productId
) {
}
