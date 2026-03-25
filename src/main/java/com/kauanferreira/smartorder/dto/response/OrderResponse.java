package com.kauanferreira.smartorder.dto.response;

import com.kauanferreira.smartorder.enums.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Order} data to the client.
 *
 * <p>Contains the order details along with its associated user, address,
 * and order items represented as nested response DTOs.</p>
 *
 * @param id          the order unique identifier
 * @param orderDate   the order creation timestamp
 * @param status      the current order status
 * @param totalAmount the order total amount
 * @param user        the associated user data
 * @param address     the delivery address data
 * @param items       the list of order items
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record OrderResponse(

        Long id,
        LocalDateTime orderDate,
        OrderStatus status,
        BigDecimal totalAmount,
        UserResponse user,
        AddressResponse address,
        List<OrderItemResponse> items
) {
}