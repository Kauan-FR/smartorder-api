package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.ChatMessageMapper;
import com.kauanferreira.smartorder.dto.request.ChatMessageRequest;
import com.kauanferreira.smartorder.dto.response.ChatMessageResponse;
import com.kauanferreira.smartorder.entity.ChatMessage;
import com.kauanferreira.smartorder.services.interfaces.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

/**
 * WebSocket STOMP controller for real-time chat messaging.
 *
 * <p>Handles messages sent via WebSocket and forwards them to the
 * appropriate recipient's topic channel. Works in conjunction with
 * {@link ChatMessageController} which handles REST-based history.</p>
 *
 * <p>Flow:
 * <ol>
 *   <li>Client sends message to {@code /app/chat.send}</li>
 *   <li>This controller processes and saves it</li>
 *   <li>Message is forwarded to {@code /topic/chat/{receiverId}}</li>
 *   <li>Receiver gets it in real time if subscribed</li>
 * </ol></p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ChatMessageService
 * @see ChatMessageController
 */
@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * Handles incoming chat messages via WebSocket.
     *
     * <p>Saves the message to the database and forwards it to the
     * receiver's personal topic channel for real-time delivery.
     * Also sends back to the sender's channel so both sides
     * of the conversation update in real time.</p>
     *
     * @param request   the chat message data
     * @param principal the authenticated user (injected by Spring Security)
     */
    @MessageMapping("/chat.send")
    public void sendMessage(@Payload ChatMessageRequest request, Principal principal) {
        ChatMessage entity = ChatMessageMapper.toEntity(request);
        ChatMessage saved = chatMessageService.send(principal.getName(), entity);

        // Reload to get full sender/receiver/product data for the response
        ChatMessage fullMessage = chatMessageService
                .getConversation(principal.getName(), request.receiverId(), request.productId())
                .stream()
                .filter(m -> m.getId().equals(saved.getId()))
                .findFirst()
                .orElse(saved);

        ChatMessageResponse response = ChatMessageMapper.toResponse(fullMessage);

        // Send to receiver's channel
        messagingTemplate.convertAndSend(
                "/topic/chat/" + request.receiverId(), response);

        // Send back to sender's channel (so their UI updates too)
        messagingTemplate.convertAndSend(
                "/topic/chat/" + fullMessage.getSender().getId(), response);
    }
}