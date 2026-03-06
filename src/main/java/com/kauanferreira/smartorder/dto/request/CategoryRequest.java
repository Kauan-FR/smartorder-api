package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for creating and updating a {@link com.kauanferreira.smartorder.entity.Category}.
 *
 * <p>Carries only the fields that the client is allowed to send.
 * Validation annotations ensure data integrity before reaching the service layer.</p>
 *
 * @param name        the category name (required, max 100 characters)
 * @param description the category description (optional, max 1000 characters)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record CategoryRequest(

        @NotBlank(message = "Category name is required")
        @Size(max = 100, message = "Category name must not exceed 100 characters")
        String name,

        @Size(max = 1000, message = "Description must not exceed 1000 characters")
        String description
) {
}
