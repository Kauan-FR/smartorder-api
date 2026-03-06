package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

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
public record ProductRequesty(

        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name must not exceed 150 characters")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description,

        @NotNull(message = "Product price is required")
        @Min(value = 0, message = "Product price must be zero or positive")
        BigDecimal price,

        @NotNull(message = "Stock quantity is required")
        @Min(value = 0, message = "Stock quantity must be zero or positive")
        Integer stockQuantity,

        @Size(max = 500, message = "Image URL must not exceed 500 characters")
        String imageUrl,

        Boolean active,

        @NotNull(message = "Category id is required")
        Long categoryId
) {
}
