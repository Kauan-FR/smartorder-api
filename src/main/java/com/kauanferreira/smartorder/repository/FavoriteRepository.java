package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Favorite} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing user's favorited products.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    /**
     * Finds all favorites belonging to a specific user.
     * Eagerly fetches the product and its category to avoid LazyInitializationException.
     *
     * @param userId the ID of the user
     * @return a list of favorites for the given user
     */
    @Query("SELECT f FROM Favorite f JOIN FETCH f.product p JOIN FETCH p.category WHERE f.user.id = :userId")
    List<Favorite> findByUserId(@Param("userId") Long userId);

    /**
     * Finds a favorite by user ID and product ID.
     * Used to check if a product is already favorited.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product
     * @return an Optional containing the favorite if found
     */
    @Query("SELECT f FROM Favorite f JOIN FETCH f.product p JOIN FETCH p.category WHERE f.user.id = :userId AND f.product.id = :productId")
    Optional<Favorite> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Deletes a favorite by user ID and product ID.
     * Used for unfavoriting a product.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product
     */
    void deleteByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);
}