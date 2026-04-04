package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.CartItem;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.CartItemRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.CartItemService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link CartItemService}.
 *
 * <p>Handles business logic for shopping cart management,
 * including duplicate detection, ownership validation,
 * and integration with user and product services.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CartItemService
 * @see CartItemRepository
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartItem> getCart(String email) {
        User user = findUserByEmail(email);
        return cartItemRepository.findByUserId(user.getId());
    }

    /**
     * {@inheritDoc}
     *
     * <p>If the product is already in the cart, the quantity is added
     * to the existing quantity rather than creating a duplicate entry.</p>
     *
     * @throws ResourceNotFoundException if the product does not exist
     */
    @Override
    @Transactional
    public CartItem addToCart(String email, CartItem cartItem) {
        User user = findUserByEmail(email);
        productService.findById(cartItem.getProduct().getId());

        Optional<CartItem> existing = cartItemRepository
                .findByUserIdAndProductId(user.getId(), cartItem.getProduct().getId());

        if (existing.isPresent()) {
            CartItem existingItem = existing.get();
            existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
            CartItem saved = cartItemRepository.save(existingItem);
            return cartItemRepository.findByUserIdAndProductId(user.getId(), saved.getProduct().getId())
                    .orElse(saved);
        }

        cartItem.setUser(user);
        CartItem saved = cartItemRepository.save(cartItem);
        entityManager.flush();
        entityManager.clear();
        return cartItemRepository.findByUserIdAndProductId(user.getId(), saved.getProduct().getId())
                .orElse(saved);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the cart item is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public CartItem updateQuantity(String email, Long cartItemId, Integer quantity) {
        User user = findUserByEmail(email);
        CartItem cartItem = findCartItemByIdAndUser(cartItemId, user.getId());
        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the cart item is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public void removeItem(String email, Long cartItemId) {
        User user = findUserByEmail(email);
        CartItem cartItem = findCartItemByIdAndUser(cartItemId, user.getId());
        cartItemRepository.delete(cartItem);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void clearCart(String email) {
        User user = findUserByEmail(email);
        cartItemRepository.deleteByUserId(user.getId());
    }

    /**
     * Finds a user by email or throws an exception.
     *
     * @param email the user's email
     * @return the found user
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with email '%s' not found", email)));
    }

    /**
     * Finds a cart item by ID and validates that it belongs to the given user.
     *
     * @param cartItemId the cart item ID
     * @param userId     the user ID
     * @return the found cart item
     * @throws ResourceNotFoundException if the cart item is not found
     *         or does not belong to the user
     */
    private CartItem findCartItemByIdAndUser(Long cartItemId, Long userId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Cart item with ID %d not found", cartItemId)));

        if (!cartItem.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    String.format("Cart item with ID %d not found", cartItemId));
        }

        return cartItem;
    }
}