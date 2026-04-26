package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.dto.projection.RatingProjection;
import com.kauanferreira.smartorder.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Review} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing product reviews.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Finds all reviews for a specific product.
     * Eagerly fetches the user and product with its category.
     *
     * @param productId the ID of the product
     * @return a list of reviews for the given product
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.product p JOIN FETCH p.category WHERE r.product.id = :productId")
    List<Review> findByProductId(@Param("productId") Long productId);

    /**
     * Finds all reviews written by a specific user.
     * Eagerly fetches the user and product with its category.
     *
     * @param userId the ID of the user
     * @return a list of reviews by the given user
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.product p JOIN FETCH p.category WHERE r.user.id = :userId")
    List<Review> findByUserId(@Param("userId") Long userId);

    /**
     * Finds a review by user ID and product ID.
     * Used to check if a user has already reviewed a product.
     *
     * @param userId    the ID of the user
     * @param productId the ID of the product
     * @return an Optional containing the review if found
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.product p JOIN FETCH p.category WHERE r.user.id = :userId AND r.product.id = :productId")
    Optional<Review> findByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

    /**
     * Finds a review by ID with eagerly fetched relationships.
     *
     * @param id the review ID
     * @return an Optional containing the review if found
     */
    @Query("SELECT r FROM Review r JOIN FETCH r.user JOIN FETCH r.product p JOIN FETCH p.category WHERE r.id = :id")
    Optional<Review> findByIdWithRelations(@Param("id") Long id);

    /**
     * Returns aggregated rating data (average and count) for a list of products
     * in a single query. Designed to avoid N+1 queries when enriching ProductResponse
     * objects with rating information.
     *
     * @param productIds list of product IDs to aggregate ratings for
     * @return list of RatingProjection — one entry per product that has at least one review.
     *         Products without reviews are NOT returned, so the caller must default missing
     *         entries to averageRating = 0.0 and reviewCount = 0.
     */
    @Query("SELECT new com.kauanferreira.smartorder.dto.projection.RatingProjection(" +
            "r.product.id, AVG(r.rating), COUNT(r)) " +
            "FROM Review r " +
            "WHERE r.product.id IN :productIds " +
            "GROUP BY r.product.id")
    List<RatingProjection> findRatingsByProductIds(@Param("productIds") List<Long> productIds);
}