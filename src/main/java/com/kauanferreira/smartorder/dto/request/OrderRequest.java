package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.validation.PositiveOrZeroDecimal;
import com.kauanferreira.smartorder.validation.ValidOrderStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO for creating and updating an {@link com.kauanferreira.smartorder.entity.Order}.
 *
 * <p>The user and address are referenced by their ids rather than full entities.</p>
 *
 * @param status      the order status (required)
 * @param totalAmount the order total amount (required, must be zero or positive)
 * @param userId      the id of the associated user (required)
 * @param addressId   the id of the delivery address (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record OrderRequest(

        @ValidOrderStatus
        OrderStatus status,

        @PositiveOrZeroDecimal(fieldName = "Order Total amount")
        BigDecimal totalAmount,

        @NotNull(message = "User id is required")
        Long userId,

        @NotNull(message = "Address id is required")
        Long addressId
) {
}
