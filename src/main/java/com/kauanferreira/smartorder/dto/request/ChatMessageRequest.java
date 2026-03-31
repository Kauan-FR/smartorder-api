package com.kauanferreira.smartorder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for sending a chat message.
 *
 * <p>The sender is not included because the backend extracts it
 * from the authenticated JWT token. Used both in REST endpoints
 * and WebSocket message handling.</p>
 *
 * @param receiverId the id of the user receiving the message (required)
 * @param productId  the id of the product the conversation is about (required)
 * @param message    the text content of the message (required)
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ChatMessageRequest(

        @NotNull(message = "Receiver ID is required")
        Long receiverId,

        @NotNull(message = "Product ID is required")
        Long productId,

        @NotBlank(message = "Message content is required")
        String message
) {
}
