package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Review;

import java.util.List;

/**
 * Service interface for managing {@link Review} operations.
 *
 * <p>Defines the contract for product review business logic.
 * Methods that modify reviews receive the authenticated user's email
 * to ensure users can only modify their own reviews.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface ReviewService {

    /**
     * Retrieves all reviews for a specific product.
     * This is a public operation that does not require authentication.
     *
     * @param productId the ID of the product
     * @return a list of reviews for the product
     */
    List<Review> getByProduct(Long productId);

    /**
     * Retrieves all reviews written by the authenticated user.
     *
     * @param email the email of the authenticated user
     * @return a list of reviews by the user
     */
    List<Review> getMyReviews(String email);

    /**
     * Creates a new review for a product.
     * Each user can only review a product once.
     *
     * @param email  the email of the authenticated user
     * @param review the review entity containing product reference, rating, and comment
     * @return the created review
     */
    Review create(String email, Review review);

    /**
     * Updates an existing review.
     * Only the review owner can update it.
     *
     * @param email    the email of the authenticated user
     * @param reviewId the ID of the review to update
     * @param review   the review entity containing updated rating and comment
     * @return the updated review
     */
    Review update(String email, Long reviewId, Review review);

    /**
     * Deletes a review.
     * Only the review owner can delete it.
     *
     * @param email    the email of the authenticated user
     * @param reviewId the ID of the review to delete
     */
    void delete(String email, Long reviewId);
}