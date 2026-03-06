package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.CategoryRequest;
import com.kauanferreira.smartorder.dto.response.CategoryResponse;
import com.kauanferreira.smartorder.entity.Category;

/**
 * Mapper class for converting between {@link Category} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CategoryRequest
 * @see CategoryResponse
 * @see Category
 */
public final class CategoryMapper {

    public CategoryMapper() {
    }

    /**
     * Converts a {@link CategoryRequest} to a {@link Category} entity.
     *
     * @param request the request DTO containing category data
     * @return a new Category entity with fields populated from the request
     */
    public static Category toEntity(CategoryRequest request) {
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        return category;
    }

    /**
     * Converts a {@link Category} entity to a {@link CategoryResponse} DTO.
     *
     * @param category the category entity
     * @return a new CategoryResponse with fields populated from the entity
     */
    public static CategoryResponse toResponse(Category category) {
        return new  CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    /**
     * Updates an existing {@link Category} entity with data from a {@link CategoryRequest}.
     *
     * <p>Used in update operations to apply new values to a managed entity.</p>
     *
     * @param request  the request DTO containing updated data
     * @param category the existing entity to update
     */
    public static void updateEntity(CategoryRequest request, Category category) {
        category.setName(request.name());
        category.setDescription(request.description());
    }
}
