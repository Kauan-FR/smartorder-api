package com.kauanferreira.smartorder.dto.response;

import com.kauanferreira.smartorder.dto.request.UserRequest;

import java.time.LocalDateTime;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.QuickReply} data to the client.
 *
 * <p>Does not include user information since quick replies are always
 * scoped to the authenticated vendor who owns them.</p>
 *
 * @param id        the quick reply unique identifier
 * @param title     the short title
 * @param message   the full message content
 * @param createdAt the timestamp when the quick reply was created
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record QuickReplyResponse(
        Long id,
        String title,
        String message,
        LocalDateTime createdAt
) {
}
