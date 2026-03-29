package com.kauanferreira.smartorder.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Favorite} data to the client.
 *
 * <p>Contains the favorite details with the full product information
 * represented as a nested {@link ProductResponse}.</p>
 *
 * @param id       the favorite unique identifier
 * @param product  the favorited product data
 * @param addedAt  the timestamp when the product was favorited
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record FavoriteResponse(
        Long id,
        ProductResponse product,
        LocalDateTime addedAt
) {
}
