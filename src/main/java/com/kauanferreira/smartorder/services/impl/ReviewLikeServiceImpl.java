package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Review;
import com.kauanferreira.smartorder.entity.ReviewLike;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.ReviewLikeRepository;
import com.kauanferreira.smartorder.repository.ReviewRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.ReviewLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of {@link ReviewLikeService}.
 *
 * <p>Handles business logic for review like management,
 * including duplicate prevention and review existence validation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ReviewLikeService
 * @see ReviewLikeRepository
 */
@Service
@RequiredArgsConstructor
public class ReviewLikeServiceImpl implements ReviewLikeService {

    private final ReviewLikeRepository reviewLikeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     *
     * <p>If the user has already liked the review, the operation
     * is silently ignored (idempotent).</p>
     *
     * @throws ResourceNotFoundException if the review does not exist
     */
    @Override
    @Transactional
    public void like(String email, Long reviewId) {
        User user = findUserByEmail(email);
        Review review = findReviewById(reviewId);

        if (reviewLikeRepository.findByUserIdAndReviewId(user.getId(), reviewId).isPresent()) {
            return;
        }

        ReviewLike reviewLike = new ReviewLike();
        reviewLike.setUser(user);
        reviewLike.setReview(review);
        reviewLikeRepository.save(reviewLike);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the review does not exist
     *         or the user has not liked it
     */
    @Override
    @Transactional
    public void unlike(String email, Long reviewId) {
        User user = findUserByEmail(email);
        findReviewById(reviewId);

        reviewLikeRepository.findByUserIdAndReviewId(user.getId(), reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("You have not liked review with ID %d", reviewId)));

        reviewLikeRepository.deleteByUserIdAndReviewId(user.getId(), reviewId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(String email, Long reviewId) {
        User user = findUserByEmail(email);
        return reviewLikeRepository.findByUserIdAndReviewId(user.getId(), reviewId).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Integer countLikes(Long reviewId) {
        return reviewLikeRepository.countByReviewId(reviewId);
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
     * Finds a review by ID or throws an exception.
     *
     * @param reviewId the review ID
     * @return the found review
     * @throws ResourceNotFoundException if no review is found with the given ID
     */
    private Review findReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Review with ID %d not found", reviewId)));
    }
}