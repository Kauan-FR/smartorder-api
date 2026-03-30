package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.entity.Review;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating and updating a {@link Review}.
 *
 * <p>The user is not included because the backend extracts it
 * from the authenticated JWT token.</p>
 *
 * @param productId the id of the product being reviewed (required)
 * @param rating    the star rating from 1 to 5 (required)
 * @param comment   optional text comment for the review
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ReviewRequest(

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotNull(message = "Rating is required")
        @Min(value = 1, message = "Rating must be at least 1")
        @Max(value = 5, message = "Rating must be at most 5")
        Integer rating,

        String comment
) {
}
