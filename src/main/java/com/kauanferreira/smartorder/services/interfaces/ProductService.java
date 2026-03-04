package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for managing {@link Product} operations.
 *
 * <p>Defines the contract for product business logic,
 * including CRUD operations, search, filtering,
 * stock management, and activation control.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface ProductService {

    /**
     * Creates a new product.
     *
     * @param product the product to create
     * @return the created product with generated ID
     */
    Product create(Product product);

    /**
     * Finds a product by its ID.
     *
     * @param id the ID of the product
     * @return the found product
     */
    Product findById(Long id);

    /**
     * Retrieves all products.
     *
     * @return a list of all products
     */
    List<Product> findAll();

    /**
     * Retrieves all products with pagination.
     *
     * @param pageable the pagination parameters
     * @return a page of products
     */
    Page<Product> findAll(Pageable pageable);

    /**
     * Retrieves all products ordered by name ascending.
     *
     * @return a list of products sorted alphabetically
     */
    List<Product> findAllOrderByNameAsc();

    /**
     * Retrieves all products ordered by price ascending.
     *
     * @return a list of products sorted by price (cheapest first)
     */
    List<Product> findAllOrderByPriceAsc();

    /**
     * Searches products by partial name match, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of matching products
     */
    List<Product> searchByName(String name);

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
     * @param active true for active, false for inactive
     * @return a list of filtered products
     */
    List<Product> findByActive(Boolean active);

    /**
     * Finds active products belonging to a specific category.
     *
     * @param categoryId the ID of the category
     * @return a list of active products in the given category
     */
    List<Product> findActiveByCategoryId(Long categoryId);

    /**
     * Finds products within a price range.
     *
     * @param minPrice minimum price (inclusive)
     * @param maxPrice maximum price (inclusive)
     * @return a list of products within the price range
     */
    List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    /**
     * Updates an existing product.
     *
     * @param id the ID of the product to update
     * @param product the updated product data
     * @return the updated product
     */
    Product update(Long id, Product product);

    /**
     * Activates a product, making it visible in the catalog.
     *
     * @param id the ID of the product to activate
     * @return the activated product
     */
    Product activate(Long id);

    /**
     * Deactivates a product, hiding it from the catalog.
     *
     * @param id the ID of the product to deactivate
     * @return the deactivated product
     */
    Product deactivate(Long id);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    void delete(Long id);
}
