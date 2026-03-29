package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.response.FavoriteResponse;
import com.kauanferreira.smartorder.entity.Favorite;

/**
 * Mapper class for converting between {@link Favorite} entity
 * and its corresponding DTO.
 *
 * <p>Only provides a toResponse method since favorites are created
 * directly in the service layer using the product ID from the URL path.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see FavoriteResponse
 * @see Favorite
 */
public final class FavoriteMapper {

    private FavoriteMapper() {
    }

    /**
     * Converts a {@link Favorite} entity to a {@link FavoriteResponse} DTO.
     *
     * <p>Uses {@link ProductMapper#toResponse} for the nested product.</p>
     *
     * @param favorite the favorite entity
     * @return a new FavoriteResponse with fields populated from the entity
     */
    public static FavoriteResponse toResponse(Favorite favorite) {
        return new FavoriteResponse(
                favorite.getId(),
                ProductMapper.toResponse(favorite.getProduct()),
                favorite.getAddedAt()
        );
    }
}
