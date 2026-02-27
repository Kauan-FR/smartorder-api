package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Product} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing products in the e-commerce catalog.</p>
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
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * Finds a product by its exact name, ignoring case.
     *
     * @param name the product name to search for
     * @return an Optional containing the product if found, or empty otherwise
     */
    Optional<Product> findByNameIgnoreCase(String name);

    /**
     * Finds products by partial name match, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of products matching the search term
     */
    List<Product> findByNameIgnoreCaseContaining(String name);

    /**
     * Finds all products belonging to a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of products in the given category
     */
    List<Product> findByCategoryId(Long categoryId);

    /**
     * Finds all active or inactive products.
     *
     * @param active true for active products, false for inactive
     * @return a list of products filtered by active status
     */
    List<Product> findByActive(Boolean active);

    /**
     * Finds active products belonging to a specific category.
     *
     * @param categoryId the ID of the category
     * @param active the active status
     * @return a list of active products in the given category
     */
    List<Product> findByCategoryIdAndActive(Long categoryId, Boolean active);

    /**
     * Finds products within a price range.
     *
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return a list of products within the price range
     */
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Finds all products ordered by price ascending.
     *
     * @return a list of products sorted by price (cheapest first)
     */
    List<Product> findAllByOrderByPriceAsc();

    /**
     * Finds all products ordered by name ascending.
     *
     * @return a list of products sorted alphabetically by name
     */
    List<Product> findAllByOrderByNameAsc();
}
