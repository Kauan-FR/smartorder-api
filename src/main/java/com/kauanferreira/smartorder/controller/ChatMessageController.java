package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.ChatMessageMapper;
import com.kauanferreira.smartorder.dto.response.ChatMessageResponse;
import com.kauanferreira.smartorder.services.interfaces.ChatMessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing chat message history.
 *
 * <p>Provides endpoints for retrieving conversation history,
 * listing all conversations, marking messages as read, and
 * counting unread messages. Real-time message sending is handled
 * by {@link ChatWebSocketController}.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ChatMessageService
 * @see ChatWebSocketController
 */
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@Tag(name = "Chat", description = "Endpoints for chat message history and management")
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    /**
     * Retrieves all conversations for the authenticated user.
     * Returns the most recent message from each unique conversation.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the list of conversations
     */
    @Operation(summary = "List conversations", description = "Retrieves the most recent message from each conversation the user is involved in.")
    @ApiResponse(responseCode = "200", description = "Conversations retrieved successfully")
    @GetMapping("/conversations")
    public ResponseEntity<List<ChatMessageResponse>> getMyConversations(Authentication authentication) {
        List<ChatMessageResponse> responses = chatMessageService.getMyConversations(authentication.getName())
                .stream()
                .map(ChatMessageMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves the message history between the authenticated user
     * and another user about a specific product.
     *
     * @param authentication the authenticated user's security context
     * @param otherUserId    the ID of the other user
     * @param productId      the ID of the product
     * @return HTTP 200 with the list of messages in chronological order
     */
    @Operation(summary = "Get conversation history", description = "Retrieves all messages between two users about a specific product.")
    @ApiResponse(responseCode = "200", description = "Messages retrieved successfully")
    @GetMapping("/messages/{otherUserId}/product/{productId}")
    public ResponseEntity<List<ChatMessageResponse>> getConversation(Authentication authentication,
                                                                     @PathVariable Long otherUserId,
                                                                     @PathVariable Long productId) {
        List<ChatMessageResponse> responses = chatMessageService
                .getConversation(authentication.getName(), otherUserId, productId)
                .stream()
                .map(ChatMessageMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Marks all unread messages from a specific sender about a product as read.
     *
     * @param authentication the authenticated user's security context
     * @param senderId       the ID of the sender
     * @param productId      the ID of the product
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Mark messages as read", description = "Marks all unread messages from a sender about a product as read.")
    @ApiResponse(responseCode = "204", description = "Messages marked as read successfully")
    @PatchMapping("/messages/read/{senderId}/product/{productId}")
    public ResponseEntity<Void> markAsRead(Authentication authentication,
                                           @PathVariable Long senderId,
                                           @PathVariable Long productId) {
        chatMessageService.markAsRead(authentication.getName(), senderId, productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Counts the total number of unread messages for the authenticated user.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the unread message count
     */
    @Operation(summary = "Count unread messages", description = "Returns the total number of unread messages for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Unread count retrieved successfully")
    @GetMapping("/unread/count")
    public ResponseEntity<Integer> countUnread(Authentication authentication) {
        return ResponseEntity.ok(chatMessageService.countUnread(authentication.getName()));
    }
}