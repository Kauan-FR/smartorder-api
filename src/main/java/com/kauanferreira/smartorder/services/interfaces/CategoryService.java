package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing {@link Category} operations.
 *
 * <p>Defines the contract for category business logic,
 * including CRUD operations and search functionality.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface CategoryService {

    /**
     * Creates a new category.
     *
     * @param category the category to create
     * @return the created category with generated ID
     */
    Category create(Category category);

    /**
     * Finds a category by its ID.
     *
     * @param id the ID of the category
     * @return the found category
     */
    Category findById(Long id);

    /**
     * Retrieves all categories.
     *
     * @return a list of all categories
     */
    List<Category> findAll();

    /**
     * Retrieves all categories with pagination.
     *
     * @param pageable the pagination parameters
     * @return a page of categories
     */
    Page<Category> findAll(Pageable pageable);

    /**
     * Retrieves all categories ordered by name ascending.
     *
     * @return a list of categories sorted alphabetically
     */
    List<Category> findAllOrderByNameAsc();

    /**
     * Searches categories by partial name match, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of matching categories
     */
    List<Category> searchByName(String name);

    /**
     * Updates an existing category.
     *
     * @param id the ID of the category to update
     * @param category the updated category data
     * @return the updated category
     */
    Category update(Long id, Category category);

    /**
     * Deletes a category by its ID.
     *
     * @param id the ID of the category to delete
     */
    void delete(Long id);
}
