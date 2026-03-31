package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.ChatMessage;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.ChatMessageRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.ChatMessageService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link ChatMessageService}.
 *
 * <p>Handles business logic for real-time chat messaging,
 * including sender validation, conversation retrieval,
 * and read status management.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ChatMessageService
 * @see ChatMessageRepository
 */
@Service
@RequiredArgsConstructor
public class ChatMessageServiceImpl implements ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ProductService productService;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the receiver or product does not exist
     */
    @Override
    @Transactional
    public ChatMessage send(String email, ChatMessage chatMessage) {
        User sender = findUserByEmail(email);
        findUserById(chatMessage.getReceiver().getId());
        productService.findById(chatMessage.getProduct().getId());

        chatMessage.setSender(sender);
        chatMessage.setIsRead(false);
        return chatMessageRepository.save(chatMessage);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Also marks all unread messages from the other user as read,
     * since opening the conversation implies the user has seen them.</p>
     */
    @Override
    @Transactional
    public List<ChatMessage> getConversation(String email, Long otherUserId, Long productId) {
        User user = findUserByEmail(email);
        chatMessageRepository.markConversationAsRead(otherUserId, user.getId(), productId);
        return chatMessageRepository.findConversation(user.getId(), otherUserId, productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getMyConversations(String email) {
        User user = findUserByEmail(email);
        return chatMessageRepository.findUserConversations(user.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void markAsRead(String email, Long senderId, Long productId) {
        User user = findUserByEmail(email);
        chatMessageRepository.markConversationAsRead(senderId, user.getId(), productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Integer countUnread(String email) {
        User user = findUserByEmail(email);
        return chatMessageRepository.countUnreadMessages(user.getId());
    }

    /**
     * Finds a user by email or throws an exception.
     *
     * @param email the user's email
     * @return the found user
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with email '%s' not found", email)));
    }

    /**
     * Finds a user by ID or throws an exception.
     *
     * @param id the user's ID
     * @return the found user
     * @throws ResourceNotFoundException if no user is found with the given ID
     */
    private User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with ID %d not found", id)));
    }
}