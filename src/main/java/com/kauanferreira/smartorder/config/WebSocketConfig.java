package com.kauanferreira.smartorder.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * WebSocket configuration for real-time chat functionality.
 *
 * <p>Configures STOMP over WebSocket with a simple in-memory
 * message broker for real-time messaging between customers
 * and sellers.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configures the message broker.
     *
     * <p>{@code /topic} — prefix for messages that the server SENDS to clients
     * (clients subscribe to /topic/chat/{userId} to receive messages).</p>
     *
     * <p>{@code /app} — prefix for messages that clients SEND to the server
     * (clients send to /app/chat.send to send a message).</p>
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registers the STOMP endpoint that clients connect to.
     *
     * <p>{@code /ws} — the WebSocket handshake URL.
     * SockJS fallback is enabled for browsers that don't support WebSocket.</p>
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}