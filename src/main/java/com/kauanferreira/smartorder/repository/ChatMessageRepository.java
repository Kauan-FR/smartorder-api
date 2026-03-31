package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link ChatMessage} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing chat messages between users.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    /**
     * Finds all messages between two users about a specific product.
     * Returns messages in chronological order (oldest first).
     * Eagerly fetches sender, receiver, and product to avoid LazyInitializationException.
     *
     * @param userId1   the ID of one user in the conversation
     * @param userId2   the ID of the other user
     * @param productId the ID of the product
     * @return a list of messages ordered by sent date ascending
     */
    @Query("SELECT m FROM ChatMessage m " +
            "JOIN FETCH m.sender " +
            "JOIN FETCH m.receiver " +
            "JOIN FETCH m.product " +
            "WHERE ((m.sender.id = :userId1 AND m.receiver.id = :userId2) " +
            "OR (m.sender.id = :userId2 AND m.receiver.id = :userId1)) " +
            "AND m.product.id = :productId " +
            "ORDER BY m.sentAt ASC")
    List<ChatMessage> findConversation(@Param("userId1") Long userId1,
                                       @Param("userId2") Long userId2,
                                       @Param("productId") Long productId);

    /**
     * Finds the most recent message for each unique conversation the user is involved in.
     * Used to build the conversations list (like WhatsApp/Shopee chat list).
     * Eagerly fetches sender, receiver, and product.
     *
     * @param userId the ID of the user
     * @return a list of the latest message from each conversation
     */
    @Query("SELECT m FROM ChatMessage m " +
            "JOIN FETCH m.sender " +
            "JOIN FETCH m.receiver " +
            "JOIN FETCH m.product " +
            "WHERE m.id IN (" +
            "   SELECT MAX(m2.id) FROM ChatMessage m2 " +
            "   WHERE m2.sender.id = :userId OR m2.receiver.id = :userId " +
            "   GROUP BY CASE WHEN m2.sender.id = :userId THEN m2.receiver.id ELSE m2.sender.id END, m2.product.id" +
            ") " +
            "ORDER BY m.sentAt DESC")
    List<ChatMessage> findUserConversations(@Param("userId") Long userId);

    /**
     * Marks all unread messages in a conversation as read.
     * Only marks messages where the given user is the receiver.
     *
     * @param senderId   the ID of the sender (the other user)
     * @param receiverId the ID of the receiver (the authenticated user)
     * @param productId  the ID of the product
     * @return the number of messages marked as read
     */
    @Modifying
    @Query("UPDATE ChatMessage m SET m.isRead = true " +
            "WHERE m.sender.id = :senderId " +
            "AND m.receiver.id = :receiverId " +
            "AND m.product.id = :productId " +
            "AND m.isRead = false")
    int markConversationAsRead(@Param("senderId") Long senderId,
                               @Param("receiverId") Long receiverId,
                               @Param("productId") Long productId);

    /**
     * Counts unread messages for a specific user.
     * Used for displaying the unread badge count.
     *
     * @param receiverId the ID of the receiver
     * @return the number of unread messages
     */
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver.id = :receiverId AND m.isRead = false")
    Integer countUnreadMessages(@Param("receiverId") Long receiverId);
}
