package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for password update requests on {@link com.kauanferreira.smartorder.entity.User}.
 *
 * <p>Requires the current password for identity verification before
 * allowing the change. This protects against unauthorized access
 * from active sessions left unattended.</p>
 *
 * @param currentPassword the user's current password for verification (required)
 * @param newPassword     the new password to set (required, min 8 characters)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record PasswordUpdateRequest(

        @NotBlank(message = "Current password is required")
        String currentPassword,

        @NotBlank(message = "New password is required")
        @Size(min = 8, message = "New password must be at least 8 characters long")
        String newPassword
) {
}
