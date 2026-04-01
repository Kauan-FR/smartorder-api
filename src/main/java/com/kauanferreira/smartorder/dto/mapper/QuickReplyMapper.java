package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.QuickReplyRequest;
import com.kauanferreira.smartorder.dto.response.QuickReplyResponse;
import com.kauanferreira.smartorder.entity.QuickReply;
import com.kauanferreira.smartorder.entity.User;

/**
 * Mapper class for converting between {@link QuickReply} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs. The user is set in the service
 * layer from the authenticated JWT token, not in the mapper.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see QuickReplyRequest
 * @see QuickReplyResponse
 * @see QuickReply
 */
public final class QuickReplyMapper {

    private QuickReplyMapper() {}

    /**
     * Converts a {@link QuickReplyRequest} to a {@link QuickReply} entity.
     *
     * <p>The user must be set separately in the service layer.</p>
     *
     * @param request the request DTO containing quick reply data
     * @return a new QuickReply entity with fields populated from the request
     */
    public static QuickReply toEntity(QuickReplyRequest request) {
        QuickReply quickReply = new QuickReply();
        quickReply.setTitle(request.title());
        quickReply.setMessage(request.message());

        return quickReply;
    }

    /**
     * Converts a {@link QuickReply} entity to a {@link QuickReplyResponse} DTO.
     *
     * @param quickReply the quick reply entity
     * @return a new QuickReplyResponse with fields populated from the entity
     */
    public static QuickReplyResponse toResponse(QuickReply quickReply) {
        return new QuickReplyResponse(
                quickReply.getId(),
                quickReply.getTitle(),
                quickReply.getMessage(),
                quickReply.getCreatedAt()
        );
    }

    /**
     * Updates an existing {@link QuickReply} entity with data from a {@link QuickReplyRequest}.
     *
     * @param request    the request DTO containing updated data
     * @param quickReply the existing entity to update
     */
    public static void updateEntity(QuickReplyRequest request, QuickReply quickReply) {
        quickReply.setTitle(request.title());
        quickReply.setMessage(request.message());
    }
}
