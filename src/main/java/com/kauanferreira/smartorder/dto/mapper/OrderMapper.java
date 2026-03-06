package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.OrderRequest;
import com.kauanferreira.smartorder.dto.response.OrderResponse;
import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.entity.User;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * Mapper class for converting between {@link Order} entity
 * and its corresponding DTOs.
 *
 * <p>Handles the nested {@link User}, {@link Address}, and
 * order items relationships.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderRequest
 * @see OrderResponse
 * @see Order
 */
public final class OrderMapper {

    private OrderMapper() {
    }

    /**
     * Converts an {@link OrderRequest} to an {@link Order} entity.
     *
     * <p>Creates User and Address references with only the id set.
     * The service layer validates that both exist.</p>
     *
     * @param request the request DTO containing order data
     * @return a new Order entity with fields populated from the request
     */
    public static Order toEntity(OrderRequest request) {
        Order order = new Order();
        order.setStatus(request.status());
        order.setTotalAmount(request.totalAmount());

        User user = new User();
        user.setId(request.userId());
        order.setUser(user);

        Address address = new Address();
        address.setId(request.addressId());
        order.setAddress(address);

        return  order;
    }

    /**
     * Converts an {@link Order} entity to an {@link OrderResponse} DTO.
     *
     * <p>Uses {@link UserMapper#toResponse}, {@link AddressMapper#toResponse},
     * and {@link OrderItemMapper#toResponse} for nested objects.</p>
     *
     * @param order the order entity
     * @return a new OrderResponse with fields populated from the entity
     */
    public static OrderResponse toResponser(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotalAmount(),
                UserMapper.toResponse(order.getUser()),
                AddressMapper.toResponse(order.getAddress()),
                order.getItems() != null
                        ? order.getItems().stream()
                        .map(OrderItemMapper::toResponse)
                        .collect(Collectors.toList())
                        : Collections.emptyList()
        );
    }

    /**
     * Updates an existing {@link Order} entity with data from an {@link OrderRequest}.
     *
     * <p>Updates status, total amount, and address. The user association
     * and order date are not updatable.</p>
     *
     * @param request the request DTO containing updated data
     * @param order   the existing entity to update
     */
    public static void updateEntity(OrderRequest request, Order order) {
        order.setStatus(request.status());
        order.setTotalAmount(request.totalAmount());

        Address address = new Address();
        address.setId(request.addressId());
        order.setAddress(address);
    }
}
