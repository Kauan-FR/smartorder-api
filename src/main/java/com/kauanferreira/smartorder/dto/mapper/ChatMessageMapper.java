package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.ChatMessageRequest;
import com.kauanferreira.smartorder.dto.response.ChatMessageResponse;
import com.kauanferreira.smartorder.entity.ChatMessage;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.entity.User;

/**
 * Mapper class for converting between {@link ChatMessage} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs. Uses lightweight user and product
 * data (id + name only) to keep WebSocket payloads small.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ChatMessageRequest
 * @see ChatMessageResponse
 * @see ChatMessage
 */
public final class ChatMessageMapper {

    private ChatMessageMapper() {}

    /**
     * Converts a {@link ChatMessageRequest} to a {@link ChatMessage} entity.
     *
     * <p>Creates User and Product references with only the id set.
     * The sender is set separately in the service layer from the JWT token.</p>
     *
     * @param request the request DTO containing message data
     * @return a new ChatMessage entity with fields populated from the request
     */
    public static ChatMessage toEntity(ChatMessageRequest request) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(request.message());
        chatMessage.setIsRead(false);

        User receiver = new User();
        receiver.setId(request.receiverId());
        chatMessage.setReceiver(receiver);

        Product product = new Product();
        product.setId(request.productId());
        chatMessage.setProduct(product);

        return chatMessage;
    }

    /**
     * Converts a {@link ChatMessage} entity to a {@link ChatMessageResponse} DTO.
     *
     * <p>Extracts only id and name from sender, receiver, and product
     * to keep the response lightweight for real-time messaging.</p>
     *
     * @param chatMessage the chat message entity
     * @return a new ChatMessageResponse with fields populated from the entity
     */
    public static ChatMessageResponse toResponse(ChatMessage chatMessage) {
        return new ChatMessageResponse(
                chatMessage.getId(),
                chatMessage.getSender().getId(),
                chatMessage.getSender().getName(),
                chatMessage.getReceiver().getId(),
                chatMessage.getReceiver().getName(),
                chatMessage.getProduct().getId(),
                chatMessage.getProduct().getName(),
                chatMessage.getMessage(),
                chatMessage.getSentAt(),
                chatMessage.getIsRead()
        );
    }
}
