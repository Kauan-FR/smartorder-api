package com.kauanferreira.smartorder.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Review} data to the client.
 *
 * <p>Contains the review details with full user and product information
 * for display in product review sections.</p>
 *
 * @param id         the review unique identifier
 * @param user       the reviewer's data
 * @param product    the reviewed product data
 * @param rating     the star rating (1-5)
 * @param comment    the review comment text
 * @param createdAt  the timestamp when the review was created
 * @param likesCount the number of likes on this review
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ReviewResponse(
        Long id,
        UserResponse user,
        ProductResponse product,
        Integer rating,
        String comment,
        LocalDateTime createdAt,
        Integer likesCount
) {
}
