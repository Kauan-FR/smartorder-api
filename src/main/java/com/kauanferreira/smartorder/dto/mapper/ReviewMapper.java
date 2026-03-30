package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.ReviewRequest;
import com.kauanferreira.smartorder.dto.response.ReviewResponse;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.entity.Review;

/**
 * Mapper class for converting between {@link Review} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs. The user is set in the service
 * layer from the authenticated JWT token, not in the mapper.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ReviewRequest
 * @see ReviewResponse
 * @see Review
 */
public final class ReviewMapper {

    private ReviewMapper() {
    }

    /**
     * Converts a {@link ReviewRequest} to a {@link Review} entity.
     *
     * <p>Creates a Product reference with only the id set. The user
     * must be set separately in the service layer.</p>
     *
     * @param request the request DTO containing review data
     * @return a new Review entity with fields populated from the request
     */
    public static Review toEntity(ReviewRequest request) {
        Review review = new Review();
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setCreatedAt(null);

        Product product = new Product();
        product.setId(request.productId());
        review.setProduct(product);

        return review;
    }

    /**
     * Converts a {@link Review} entity to a {@link ReviewResponse} DTO.
     *
     * <p>Uses {@link UserMapper#toResponse} and {@link ProductMapper#toResponse}
     * for the nested objects. Receives the likes count as a parameter
     * since the mapper is a static utility class without dependency injection.</p>
     *
     * @param review     the review entity
     * @param likesCount the number of likes for this review
     * @return a new ReviewResponse with fields populated from the entity
     */
    public static ReviewResponse toResponse(Review review, Integer likesCount) {
        return new ReviewResponse(
                review.getId(),
                UserMapper.toResponse(review.getUser()),
                ProductMapper.toResponse(review.getProduct()),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                likesCount
        );
    }

    /**
     * Updates an existing {@link Review} entity with data from a {@link ReviewRequest}.
     *
     * @param request the request DTO containing updated data
     * @param review  the existing entity to update
     */
    public static void updateEntity(Review review, ReviewRequest request) {
        review.setRating(request.rating());
        review.setComment(request.comment());
    }
}
