package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Review;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.DuplicateResourceException;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.ReviewRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import com.kauanferreira.smartorder.services.interfaces.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ReviewService}.
 *
 * <p>Handles business logic for product review management,
 * including duplicate prevention, ownership validation,
 * and integration with user and product services.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ReviewService
 * @see ReviewRepository
 */
@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the product does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<Review> getByProduct(Long productId) {
        productService.findById(productId);
        return reviewRepository.findByProductId(productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Review> getMyReviews(String email) {
        User user = findUserByEmail(email);
        return reviewRepository.findByUserId(user.getId());
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException  if the product does not exist
     * @throws DuplicateResourceException if the user has already reviewed this product
     */
    @Override
    @Transactional
    public Review create(String email, Review review) {
        User user = findUserByEmail(email);
        productService.findById(review.getProduct().getId());

        reviewRepository.findByUserIdAndProductId(user.getId(), review.getProduct().getId())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            String.format("You have already reviewed product with ID %d", review.getProduct().getId()));
                });

        review.setUser(user);
        return reviewRepository.save(review);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the review is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public Review update(String email, Long reviewId, Review review) {
        User user = findUserByEmail(email);
        Review existing = findReviewByIdAndUser(reviewId, user.getId());
        existing.setRating(review.getRating());
        existing.setComment(review.getComment());
        return reviewRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the review is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public void delete(String email, Long reviewId) {
        User user = findUserByEmail(email);
        Review existing = findReviewByIdAndUser(reviewId, user.getId());
        reviewRepository.delete(existing);
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
     * Finds a review by ID and validates that it belongs to the given user.
     *
     * @param reviewId the review ID
     * @param userId   the user ID
     * @return the found review
     * @throws ResourceNotFoundException if the review is not found
     *         or does not belong to the user
     */
    private Review findReviewByIdAndUser(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Review with ID %d not found", reviewId)));

        if (!review.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    String.format("Review with ID %d not found", reviewId));
        }

        return review;
    }
}