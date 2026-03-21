package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Request DTO for user authentication (login).
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record AuthRequent(
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Password is required")
        String password
) {
}
