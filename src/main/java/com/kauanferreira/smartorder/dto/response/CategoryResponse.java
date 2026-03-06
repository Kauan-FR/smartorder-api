package com.kauanferreira.smartorder.dto.response;

/**
 * DTO for returning {@link com.kauanferreira.smartorder.entity.Category} data to the client.
 *
 * <p>Contains only the fields that should be exposed through the API.</p>
 *
 * @param id          the category unique identifier
 * @param name        the category name
 * @param description the category description
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public record CategoryResponse(

        Long id,
        String name,
        String description
) {
}
