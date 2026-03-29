package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.CartItem;

import java.util.List;

/**
 * Service interface for managing {@link CartItem} operations.
 *
 * <p>Defines the contract for shopping cart business logic.
 * All methods receive the authenticated user's email to ensure
 * users can only access and modify their own cart.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface CartItemService {

    /**
     * Retrieves all cart items for the authenticated user.
     *
     * @param email the email of the authenticated user
     * @return a list of cart items belonging to the user
     */
    List<CartItem> getCart(String email);

    /**
     * Adds a product to the user's cart.
     * If the product is already in the cart, updates the quantity instead.
     *
     * @param email   the email of the authenticated user
     * @param cartItem the cart item containing product reference and quantity
     * @return the created or updated cart item
     */
    CartItem addToCart(String email, CartItem cartItem);

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param email      the email of the authenticated user
     * @param cartItemId the ID of the cart item to update
     * @param quantity   the new quantity
     * @return the updated cart item
     */
    CartItem updateQuantity(String email, Long cartItemId, Integer quantity);

    /**
     * Removes a single item from the user's cart.
     *
     * @param email      the email of the authenticated user
     * @param cartItemId the ID of the cart item to remove
     */
    void removeItem(String email, Long cartItemId);

    /**
     * Removes all items from the user's cart.
     *
     * @param email the email of the authenticated user
     */
    void clearCart(String email);
}