package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.ReviewMapper;
import com.kauanferreira.smartorder.dto.request.ReviewRequest;
import com.kauanferreira.smartorder.dto.response.ReviewResponse;
import com.kauanferreira.smartorder.entity.Review;
import com.kauanferreira.smartorder.services.interfaces.ReviewLikeService;
import com.kauanferreira.smartorder.services.interfaces.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing product reviews.
 *
 * <p>Provides public endpoints for viewing reviews and authenticated
 * endpoints for creating, updating, and deleting reviews.
 * Users can only modify their own reviews.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ReviewService
 * @see ReviewMapper
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Reviews", description = "Endpoints for managing product reviews")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewLikeService reviewLikeService;

    /**
     * Creates a new review for a product.
     * Each user can only review a product once.
     *
     * @param authentication the authenticated user's security context
     * @param request        the review data (product ID, rating, comment)
     * @return HTTP 201 with the created review and location header
     */
    @Operation(summary = "Create a review", description = "Creates a new review for a product. Each user can only review a product once.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Review created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found"),
            @ApiResponse(responseCode = "409", description = "User has already reviewed this product")
    })
    @PostMapping
    public ResponseEntity<ReviewResponse> create(Authentication authentication,
                                                 @Valid @RequestBody ReviewRequest request) {
        Review entity = ReviewMapper.toEntity(request);
        Review created = reviewService.create(authentication.getName(), entity);
        ReviewResponse response = ReviewMapper.toResponse(created, reviewLikeService.countLikes(created.getId()));

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves all reviews for a specific product.
     * This endpoint is public and does not require authentication.
     *
     * @param productId the ID of the product
     * @return HTTP 200 with the list of reviews
     */
    @Operation(summary = "Get reviews by product", description = "Retrieves all reviews for a specific product. Public endpoint.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getByProduct(@PathVariable Long productId) {
        List<ReviewResponse> responses = reviewService.getByProduct(productId)
                .stream()
                .map(review -> ReviewMapper.toResponse(review, reviewLikeService.countLikes(review.getId())))                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all reviews written by the authenticated user.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the list of the user's reviews
     */
    @Operation(summary = "Get my reviews", description = "Retrieves all reviews written by the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Reviews retrieved successfully")
    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Authentication authentication) {
        List<ReviewResponse> responses = reviewService.getMyReviews(authentication.getName())
                .stream()
                .map(review -> ReviewMapper.toResponse(review, reviewLikeService.countLikes(review.getId())))                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates an existing review. Only the review owner can update it.
     *
     * @param authentication the authenticated user's security context
     * @param id             the review ID
     * @param request        the updated review data (rating, comment)
     * @return HTTP 200 with the updated review
     */
    @Operation(summary = "Update a review", description = "Updates an existing review. Only the review owner can update it.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Review updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(Authentication authentication,
                                                 @PathVariable Long id,
                                                 @Valid @RequestBody ReviewRequest request) {
        Review entity = ReviewMapper.toEntity(request);
        Review updated = reviewService.update(authentication.getName(), id, entity);
        return ResponseEntity.ok(ReviewMapper.toResponse(updated, reviewLikeService.countLikes(updated.getId())));
    }

    /**
     * Deletes a review. Only the review owner can delete it.
     *
     * @param authentication the authenticated user's security context
     * @param id             the review ID
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete a review", description = "Deletes a review. Only the review owner can delete it.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(Authentication authentication,
                                       @PathVariable Long id) {
        reviewService.delete(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }
}