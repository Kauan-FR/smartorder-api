package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Favorite;

import java.util.List;

/**
 * Service interface for managing {@link Favorite} operations.
 *
 * <p>Defines the contract for favorites/wishlist business logic.
 * All methods receive the authenticated user's email to ensure
 * users can only access and modify their own favorites.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface FavoriteService {

    /**
     * Retrieves all favorited products for the authenticated user.
     *
     * @param email the email of the authenticated user
     * @return a list of favorites belonging to the user
     */
    List<Favorite> getFavorites(String email);

    /**
     * Adds a product to the user's favorites.
     * If the product is already favorited, returns the existing favorite.
     *
     * @param email     the email of the authenticated user
     * @param productId the ID of the product to favorite
     * @return the created or existing favorite
     */
    Favorite addFavorite(String email, Long productId);

    /**
     * Removes a product from the user's favorites.
     *
     * @param email     the email of the authenticated user
     * @param productId the ID of the product to unfavorite
     */
    void removeFavorite(String email, Long productId);

    /**
     * Checks if a product is favorited by the authenticated user.
     *
     * @param email     the email of the authenticated user
     * @param productId the ID of the product to check
     * @return true if the product is favorited, false otherwise
     */
    boolean isFavorited(String email, Long productId);
}