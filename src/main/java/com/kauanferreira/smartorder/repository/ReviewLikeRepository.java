package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link ReviewLike} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing likes on product reviews.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {

    /**
     * Finds a like by user ID and review ID.
     * Used to check if a user has already liked a review.
     *
     * @param userId   the ID of the user
     * @param reviewId the ID of the review
     * @return an Optional containing the like if found
     */
    Optional<ReviewLike> findByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    /**
     * Returns the IDs of all reviews liked by the given user.
     * Used by the product detail page to mark which review-like buttons
     * should appear in their "liked" state on initial render.
     *
     * @param userId the authenticated user's ID
     * @return list of review IDs that the user has liked
     */
    @Query("SELECT rl.review.id FROM ReviewLike rl WHERE rl.user.id = :userId")
    List<Long> findLikedReviewIdsByUserId(@Param("userId") Long userId);

    /**
     * Deletes a like by user ID and review ID.
     * Used for unliking a review.
     *
     * @param userId   the ID of the user
     * @param reviewId the ID of the review
     */
    void deleteByUserIdAndReviewId(@Param("userId") Long userId, @Param("reviewId") Long reviewId);

    /**
     * Counts the total number of likes for a specific review.
     *
     * @param reviewId the ID of the review
     * @return the number of likes
     */
    Integer countByReviewId(@Param("reviewId") Long reviewId);
}