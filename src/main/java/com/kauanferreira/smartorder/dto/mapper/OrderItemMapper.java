package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.OrderItemRequest;
import com.kauanferreira.smartorder.dto.response.OrderItemResponse;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.entity.OrderItem;
import com.kauanferreira.smartorder.entity.Product;

/**
 * Mapper class for converting between {@link OrderItem} entity
 * and its corresponding DTOs.
 *
 * <p>Handles the nested {@link Order} and {@link Product} relationships.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderItemRequest
 * @see OrderItemResponse
 * @see OrderItem
 */
public final class OrderItemMapper {

    public OrderItemMapper() {
    }

     /**
     * Converts an {@link OrderItemRequest} to an {@link OrderItem} entity.
     *
     * <p>Creates Order and Product references with only the id set.
     * The service layer validates that both exist.</p>
     *
     * @param request the request DTO containing order item data
     * @return a new OrderItem entity with fields populated from the request
     */
    public static OrderItem toEntity(OrderItemRequest request) {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(request.quantity());
        orderItem.setPrice(request.price());
        orderItem.setSubtotal(request.subtotal());

        Order order = new Order();
        order.setId(request.orderId());
        orderItem.setOrder(order);

        Product product = new Product();
        product.setId(request.productId());
        orderItem.setProduct(product);

        return  orderItem;
    }

    /**
     * Converts an {@link OrderItem} entity to an {@link OrderItemResponse} DTO.
     *
     * <p>Uses {@link ProductMapper#toResponse} for the nested product.</p>
     *
     * @param orderItem the order item entity
     * @return a new OrderItemResponse with fields populated from the entity
     */
    public static OrderItemResponse toResponse(OrderItem orderItem) {
        return new OrderItemResponse(
                orderItem.getId(),
                orderItem.getQuantity(),
                orderItem.getPrice(),
                orderItem.getSubtotal(),
                ProductMapper.toResponse(orderItem.getProduct())
        );
    }


    /**
     * Updates an existing {@link OrderItem} entity with data from an {@link OrderItemRequest}.
     *
     * <p>Updates quantity, price, subtotal, and product.
     * The order association is not updatable.</p>
     *
     * @param request   the request DTO containing updated data
     * @param orderItem the existing entity to update
     */
    public static void updateEntity(OrderItemRequest request, OrderItem orderItem) {
        orderItem.setQuantity(request.quantity());
        orderItem.setPrice(request.price());
        orderItem.setSubtotal(request.subtotal());

        Product product = new Product();
        product.setId(request.productId());
        orderItem.setProduct(product);
    }
}
