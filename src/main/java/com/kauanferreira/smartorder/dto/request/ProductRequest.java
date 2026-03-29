package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.validation.NotBlankField;
import com.kauanferreira.smartorder.validation.PositiveOrZeroDecimal;
import com.kauanferreira.smartorder.validation.PositiveOrZeroInteger;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO for creating and updating a {@link com.kauanferreira.smartorder.entity.Product}.
 *
 * <p>Carries only the fields that the client is allowed to send.
 * The category is referenced by its id rather than the full entity.</p>
 *
 * @param name           the product name (required, max 150 characters)
 * @param description    the product description (optional, max 2000 characters)
 * @param price          the product price (required, must be zero or positive)
 * @param stockQuantity  the available stock quantity (required, must be zero or positive)
 * @param imageUrl       the product image URL (optional, max 500 characters)
 * @param active         whether the product is active (defaults to true if null)
 * @param categoryId     the id of the associated category (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ProductRequest(

        @NotBlankField(fieldName = "Product name", maxLength = 150)
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @PositiveOrZeroDecimal(fieldName = "Price")
        BigDecimal price,

        @PositiveOrZeroInteger(fieldName = "Product Stock quantity")
        Integer stockQuantity,

        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl,

        Boolean active,

        @Max(value = 100, message = "Discount percent must not exceed 100")
        @PositiveOrZeroInteger(fieldName = "Discount percent")
        Integer discountPercent,

        @PositiveOrZeroInteger(fieldName = "Initial stock quantity")
        Integer initialStock,

        LocalDateTime dealExpiresAt,

        Boolean featured,

        @NotNull(message = "Category id is required")
        Long categoryId
) {
}
