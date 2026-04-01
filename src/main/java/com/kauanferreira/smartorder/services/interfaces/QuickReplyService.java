package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.QuickReply;

import java.util.List;

/**
 * Service interface for managing {@link QuickReply} operations.
 *
 * <p>Defines the contract for quick reply business logic.
 * All methods receive the authenticated user's email to ensure
 * vendors can only access and modify their own quick replies.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface QuickReplyService {

    /**
     * Retrieves all quick replies for the authenticated vendor.
     *
     * @param email the email of the authenticated user
     * @return a list of quick replies belonging to the vendor
     */
    List<QuickReply> getMyQuickReplies(String email);

    /**
     * Creates a new quick reply for the authenticated vendor.
     *
     * @param email      the email of the authenticated user
     * @param quickReply the quick reply entity containing title and message
     * @return the created quick reply
     */
    QuickReply create(String email, QuickReply quickReply);

    /**
     * Updates an existing quick reply.
     * Only the owner can update it.
     *
     * @param email        the email of the authenticated user
     * @param quickReplyId the ID of the quick reply to update
     * @param quickReply   the quick reply entity containing updated title and message
     * @return the updated quick reply
     */
    QuickReply update(String email, Long quickReplyId, QuickReply quickReply);

    /**
     * Deletes a quick reply.
     * Only the owner can delete it.
     *
     * @param email        the email of the authenticated user
     * @param quickReplyId the ID of the quick reply to delete
     */
    void delete(String email, Long quickReplyId);
}