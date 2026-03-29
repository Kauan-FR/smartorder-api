package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.CartItemRequest;
import com.kauanferreira.smartorder.dto.response.CartItemResponse;
import com.kauanferreira.smartorder.entity.CartItem;
import com.kauanferreira.smartorder.entity.Product;

/**
 * Mapper class for converting between {@link CartItem} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs. The user is set in the service
 * layer from the authenticated JWT token, not in the mapper.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CartItemRequest
 * @see CartItemResponse
 * @see CartItem
 */
public final class CartItemMapper {

    private CartItemMapper() {
    }

    /**
     * Converts a {@link CartItemRequest} to a {@link CartItem} entity.
     *
     * <p>Creates a Product reference with only the id set. The user
     * must be set separately in the service layer.</p>
     *
     * @param request the request DTO containing cart item data
     * @return a new CartItem entity with fields populated from the request
     */
    public static CartItem toEntity(CartItemRequest request) {
        CartItem cartItem = new CartItem();
        cartItem.setQuantity(request.quantity());
        cartItem.setAddedAt(java.time.LocalDateTime.now());

        Product product = new Product();
        product.setId(request.productId());
        cartItem.setProduct(product);

        return cartItem;
    }

    /**
     * Converts a {@link CartItem} entity to a {@link CartItemResponse} DTO.
     *
     * <p>Uses {@link ProductMapper#toResponse} for the nested product.</p>
     *
     * @param cartItem the cart item entity
     * @return a new CartItemResponse with fields populated from the entity
     */
    public static CartItemResponse toResponse(CartItem cartItem) {
        return new CartItemResponse(
                cartItem.getId(),
                ProductMapper.toResponse(cartItem.getProduct()),
                cartItem.getQuantity(),
                cartItem.getAddedAt()
        );
    }

    /**
     * Updates an existing {@link CartItem} entity with data from a {@link CartItemRequest}.
     *
     * @param request  the request DTO containing updated quantity
     * @param cartItem the existing entity to update
     */
    public static void updateEntity(CartItemRequest request, CartItem cartItem) {
        cartItem.setQuantity(request.quantity());
    }
}
