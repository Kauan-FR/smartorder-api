package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.services.interfaces.ReviewLikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for managing likes on product reviews.
 *
 * <p>All endpoints require authentication and operate based on
 * the currently authenticated user's JWT token.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ReviewLikeService
 */
@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@Tag(name = "Review Likes", description = "Endpoints for liking and unliking reviews")
public class ReviewLikeController {

    private final ReviewLikeService reviewLikeService;

    /**
     * Likes a review. If already liked, the operation is silently ignored.
     *
     * @param authentication the authenticated user's security context
     * @param reviewId       the ID of the review to like
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Like a review", description = "Likes a review. If already liked, the operation is ignored.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review liked successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found")
    })
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<Void> like(Authentication authentication,
                                     @PathVariable Long reviewId) {
        reviewLikeService.like(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Checks if the authenticated user has liked a review.
     *
     * @param authentication the authenticated user's security context
     * @param reviewId       the ID of the review to check
     * @return HTTP 200 with true if liked, false otherwise
     */
    @Operation(summary = "Check if liked", description = "Checks if the authenticated user has liked a review.")
    @ApiResponse(responseCode = "200", description = "Like status retrieved successfully")
    @GetMapping("/{reviewId}/like/check")
    public ResponseEntity<Boolean> isLiked(Authentication authentication,
                                           @PathVariable Long reviewId) {
        return ResponseEntity.ok(reviewLikeService.isLiked(authentication.getName(), reviewId));
    }

    /**
     * Unlikes a review.
     *
     * @param authentication the authenticated user's security context
     * @param reviewId       the ID of the review to unlike
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Unlike a review", description = "Removes a like from a review.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Review unliked successfully"),
            @ApiResponse(responseCode = "404", description = "Review not found or not liked")
    })
    @DeleteMapping("/{reviewId}/like")
    public ResponseEntity<Void> unlike(Authentication authentication,
                                       @PathVariable Long reviewId) {
        reviewLikeService.unlike(authentication.getName(), reviewId);
        return ResponseEntity.noContent().build();
    }

}