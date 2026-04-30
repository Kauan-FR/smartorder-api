package com.kauanferreira.smartorder.services.interfaces;

import java.util.List;

/**
 * Service interface for managing {@link com.kauanferreira.smartorder.entity.ReviewLike} operations.
 *
 * <p>Defines the contract for review like business logic.
 * All methods receive the authenticated user's email to ensure
 * proper ownership tracking.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface ReviewLikeService {

    /**
     * Likes a review. If already liked, the operation is ignored.
     *
     * @param email    the email of the authenticated user
     * @param reviewId the ID of the review to like
     */
    void like(String email, Long reviewId);

    /**
     * Unlikes a review.
     *
     * @param email    the email of the authenticated user
     * @param reviewId the ID of the review to unlike
     */
    void unlike(String email, Long reviewId);

    /**
     * Checks if a review is liked by the authenticated user.
     *
     * @param email    the email of the authenticated user
     * @param reviewId the ID of the review to check
     * @return true if the review is liked, false otherwise
     */
    boolean isLiked(String email, Long reviewId);

    /**
     * Returns the IDs of all reviews the given user has liked.
     *
     * @param email the authenticated user's email (from JWT)
     * @return list of review IDs liked by the user
     */
    List<Long> getLikedReviewIds(String email);

    /**
     * Counts the total number of likes for a review.
     *
     * @param reviewId the ID of the review
     * @return the number of likes
     */
    Integer countLikes(Long reviewId);
}