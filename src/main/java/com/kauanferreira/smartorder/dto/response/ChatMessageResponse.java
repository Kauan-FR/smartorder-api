package com.kauanferreira.smartorder.dto.response;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.ChatMessage} data to the client.
 *
 * <p>Contains the message details with sender and receiver names
 * for display in the chat interface. Uses simple user info (id + name)
 * instead of full UserResponse to keep chat payloads lightweight.</p>
 *
 * @param id           the message unique identifier
 * @param senderId     the sender's user ID
 * @param senderName   the sender's display name
 * @param receiverId   the receiver's user ID
 * @param receiverName the receiver's display name
 * @param productId    the product ID the conversation is about
 * @param productName  the product name for display context
 * @param message      the text content of the message
 * @param sentAt       the timestamp when the message was sent
 * @param isRead       whether the receiver has read the message
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record ChatMessageResponse(
        Long id,
        Long senderId,
        String senderName,
        Long receiverId,
        String receiverName,
        Long productId,
        String productName,
        String message,
        LocalDateTime sentAt,
        Boolean isRead
) {
}
