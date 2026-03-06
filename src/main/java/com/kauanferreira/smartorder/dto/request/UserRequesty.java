package com.kauanferreira.smartorder.dto.request;

import com.kauanferreira.smartorder.enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO for creating and updating a {@link com.kauanferreira.smartorder.entity.User}.
 *
 * <p>Password is required on creation but should be handled separately
 * on update through a dedicated endpoint.</p>
 *
 * @param name     the user name (required, max 100 characters)
 * @param email    the user email (required, must be valid format, max 150 characters)
 * @param password the user password (required on creation, max 255 characters)
 * @param role     the user role (required)
 * @param phone    the user phone number (optional, max 20 characters)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record UserRequesty(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Size(max = 150, message = "Email must not exceed 150 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max = 255, message = "Password must not exceed 255 characters")
        String password,

        @NotNull(message = "Role is required")
        Role role,

        @Size(max = 20, message = "Phone number must not exceed 20 characters")
        String phone
) {
}
