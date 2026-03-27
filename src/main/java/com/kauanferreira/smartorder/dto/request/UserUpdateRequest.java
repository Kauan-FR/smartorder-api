package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.validation.NotBlankField;
import com.kauanferreira.smartorder.validation.ValidEmail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Data Transfer Object for updating an existing user.
 * <p>
 * This record is used exclusively for {@code PUT} operations where
 * password changes are not allowed. Password updates are handled
 * separately via the {@code PATCH /{id}/password} endpoint.
 * </p>
 *
 * @param name  the user's full name (required, max 100 characters)
 * @param email the user's email address (required, must be valid format)
 * @param role  the user's role (required: ADMIN or CUSTOMER)
 * @param phone the user's phone number (optional, max 20 characters)
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
public record UserUpdateRequest(

        @NotBlankField(fieldName = "User name", maxLength = 100)
        String name,

        @ValidEmail
        String email,

        @NotNull(message = "Role is required")
        Role role,

        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phone
) {
}
