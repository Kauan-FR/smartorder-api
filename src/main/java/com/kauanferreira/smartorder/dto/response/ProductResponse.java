package com.kauanferreira.smartorder.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Product} data to the client.
 *
 * <p>Contains the product details along with its associated category
 * represented as a nested {@link CategoryResponse}.</p>
 *
 * @param id             the product unique identifier
 * @param name           the product name
 * @param description    the product description
 * @param price          the product price
 * @param stockQuantity  the available stock quantity
 * @param imageUrl       the product image URL
 * @param active         whether the product is active
 * @param category       the associated category data
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ProductResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        Boolean active,
        Integer discountPercent,
        Integer initialStock,
        LocalDateTime dealExpiresAt,
        Boolean featured,
        BigDecimal finalPrice,
        CategoryResponse category
) {
}
