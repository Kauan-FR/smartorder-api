package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link CartItem} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing shopping cart items.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product p JOIN FETCH p.category JOIN FETCH ci.user WHERE ci.id = :id")
    Optional<CartItem> findById(@Param("id") Long id);

    /**
     * Finds all cart items belonging to a specific user.
     * Eagerly fetches the product and its category to avoid LazyInitializationException.
     *
     * @param userId the ID of the user
     * @return a list of cart items for the given user
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product p JOIN FETCH p.category WHERE ci.user.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    /**
     * Finds a cart item by user ID and product ID.
     * Used to check if a product is already in the user's cart.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product
     * @return an Optional containing the cart item if found
     */
    @Query("SELECT ci FROM CartItem ci JOIN FETCH ci.product p JOIN FETCH p.category JOIN FETCH ci.user WHERE ci.user.id = :userId AND ci.product.id = :productId")
    Optional<CartItem> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Deletes all cart items belonging to a specific user.
     * Used for clearing the entire cart.
     *
     * @param userId the ID of the user
     */
    void deleteByUserId(@Param("userId") Long userId);
}