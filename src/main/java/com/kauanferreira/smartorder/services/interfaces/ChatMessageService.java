package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.ChatMessage;

import java.util.List;

/**
 * Service interface for managing {@link ChatMessage} operations.
 *
 * <p>Defines the contract for real-time chat business logic.
 * Methods receive the authenticated user's email to ensure
 * users can only access their own conversations.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface ChatMessageService {

    /**
     * Sends a new chat message.
     *
     * @param email       the email of the authenticated sender
     * @param chatMessage the message entity containing receiver, product, and content
     * @return the saved message with generated ID and timestamp
     */
    ChatMessage send(String email, ChatMessage chatMessage);

    /**
     * Retrieves the message history between the authenticated user
     * and another user about a specific product.
     *
     * @param email       the email of the authenticated user
     * @param otherUserId the ID of the other user in the conversation
     * @param productId   the ID of the product the conversation is about
     * @return a list of messages in chronological order
     */
    List<ChatMessage> getConversation(String email, Long otherUserId, Long productId);

    /**
     * Retrieves all conversations for the authenticated user.
     * Returns the most recent message from each unique conversation.
     *
     * @param email the email of the authenticated user
     * @return a list of the latest message from each conversation
     */
    List<ChatMessage> getMyConversations(String email);

    /**
     * Marks all unread messages in a conversation as read.
     *
     * @param email       the email of the authenticated user (receiver)
     * @param senderId    the ID of the sender
     * @param productId   the ID of the product
     */
    void markAsRead(String email, Long senderId, Long productId);

    /**
     * Counts unread messages for the authenticated user.
     *
     * @param email the email of the authenticated user
     * @return the number of unread messages
     */
    Integer countUnread(String email);
}
