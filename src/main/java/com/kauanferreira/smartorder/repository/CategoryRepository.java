package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Category} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing product categories in the database.</p>
 *
 * <p>Inherits from {@link JpaRepository}, which already provides:
 * {@code save()}, {@code findById()}, {@code findAll()},
 * {@code deleteById()}, {@code count()}, and pagination support.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Finds categories by their name, ignoring case.
     *
     * @param name the name of the category to search for
     * @return a list of categories matching the given name
     */
    Optional<Category> findByNameIgnoreCase(String name);

    /**
     * Finds categories whose names contain the specified substring, ignoring case.
     *
     * @param name the substring to search for in category names
     * @return a list of categories whose names contain the specified substring
     */
    List<Category> findByNameContainingIgnoreCase(String name);


    /**
     * Retrieves all categories ordered alphabetically by name.
     *
     * @return a list of categories sorted by name in ascending order
     */
    List<Category> findAllByOrderByNameAsc();
}
