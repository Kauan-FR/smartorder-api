package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
 * {@code delete()}, {@code count()}, and pagination support.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN FETCH p.category")
    List<Product> findAll();

    @Query(value = "SELECT p FROM Product p JOIN FETCH p.category",
            countQuery = "SELECT COUNT(p) FROM Product p")
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.id = :id")
    Optional<Product> findById(@Param("id") Long id);
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
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Product> findByNameContainingIgnoreCase(@Param("name") String name);

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
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.active = :active")
    List<Product> findByActive(@Param("active") Boolean active);

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
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.price BETWEEN :minPrice AND :maxPrice")
    List<Product> findByPriceBetween(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);

    /**
     * Finds all products ordered by price ascending.
     *
     * @return a list of products sorted by price (cheapest first)
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.price ASC")
    List<Product> findAllByOrderByPriceAsc();

    /**
     * Finds all products ordered by name ascending.
     *
     * @return a list of products sorted alphabetically by name
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.name ASC")
    List<Product> findAllByOrderByNameAsc();

    /**
     * Finds all products with an active deal (discount > 0 and expiration in the future).
     *
     * @param percent the minimum discount percent (exclusive)
     * @param now the current date/time to compare against deal expiration
     * @return a list of products with active deals
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.discountPercent > :percent AND p.dealExpiresAt > :now")
    List<Product> findActiveDeals(@Param("percent") Integer percent, @Param("now") LocalDateTime now);

    /**
     * Finds all featured products for highlighted sections.
     *
     * @return a list of featured products
     */
    @Query("SELECT p FROM Product p JOIN FETCH p.category WHERE p.featured = true")
    List<Product> findByFeaturedTrue();
}
