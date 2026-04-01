package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO for creating and updating a {@link com.kauanferreira.smartorder.entity.QuickReply}.
 *
 * <p>The user is not included because the backend extracts it
 * from the authenticated JWT token.</p>
 *
 * @param title   the short title for the quick reply (required, max 100 characters)
 * @param message the full message content (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record QuickReplyRequest(

        @NotBlank(message = "Quick reply title is required")
        @Size(max = 100, message = "Title must not exceed 100 characters")
        String title,

        @NotNull(message = "Quick reply message is required")
        String message
) {
}
