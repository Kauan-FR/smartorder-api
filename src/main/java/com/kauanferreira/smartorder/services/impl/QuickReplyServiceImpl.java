package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.QuickReply;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.QuickReplyRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.QuickReplyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link QuickReplyService}.
 *
 * <p>Handles business logic for quick reply management,
 * including ownership validation to ensure vendors can
 * only manage their own quick replies.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see QuickReplyService
 * @see QuickReplyRepository
 */
@Service
@RequiredArgsConstructor
public class QuickReplyServiceImpl implements QuickReplyService {

    private final QuickReplyRepository quickReplyRepository;
    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<QuickReply> getMyQuickReplies(String email) {
        User user = findUserByEmail(email);
        return quickReplyRepository.findByUserId(user.getId());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public QuickReply create(String email, QuickReply quickReply) {
        User user = findUserByEmail(email);
        quickReply.setUser(user);
        return quickReplyRepository.save(quickReply);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the quick reply is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public QuickReply update(String email, Long quickReplyId, QuickReply quickReply) {
        User user = findUserByEmail(email);
        QuickReply existing = findByIdAndUser(quickReplyId, user.getId());
        existing.setTitle(quickReply.getTitle());
        existing.setMessage(quickReply.getMessage());
        return quickReplyRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the quick reply is not found
     *         or does not belong to the authenticated user
     */
    @Override
    @Transactional
    public void delete(String email, Long quickReplyId) {
        User user = findUserByEmail(email);
        QuickReply existing = findByIdAndUser(quickReplyId, user.getId());
        quickReplyRepository.delete(existing);
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
     * Finds a quick reply by ID and validates that it belongs to the given user.
     *
     * @param quickReplyId the quick reply ID
     * @param userId       the user ID
     * @return the found quick reply
     * @throws ResourceNotFoundException if the quick reply is not found
     *         or does not belong to the user
     */
    private QuickReply findByIdAndUser(Long quickReplyId, Long userId) {
        QuickReply quickReply = quickReplyRepository.findById(quickReplyId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Quick reply with ID %d not found", quickReplyId)));

        if (!quickReply.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException(
                    String.format("Quick reply with ID %d not found", quickReplyId));
        }

        return quickReply;
    }
}