package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.dto.response.ProductResponse;
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
     * Updates an existing product.
     *
     * @param id the ID of the product to update
     * @param product the updated product data
     * @return the updated product
     */
    Product update(Long id, Product product);

    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete
     */
    void delete(Long id);

    /**
     * Finds all products with an active deal (discount applied and not expired).
     *
     * @return a list of products currently on sale
     */
    List<Product> findActiveDeals();

    /**
     * Finds all featured products for highlighted store sections.
     *
     * @return a list of featured products
     */
    List<Product> findFeatured();

    /**
     * Returns all active products that are running low on stock.
     * Used by the storefront carousel to highlight products about to sell out.
     *
     * @return list of low-stock products
     */
    List<Product> findLowStock();

    /**
     * Returns up to 5 random featured products for the storefront carousel.
     *
     * @return list of up to 5 random featured products
     */
    List<Product> findFeaturedRandom();

    /**
     * Returns up to 5 random products with active discount for the storefront carousel.
     *
     * @return list of up to 5 random products with active deals
     */
    List<Product> findDealsRandom();

    /**
     * Returns up to 5 random low-stock products for the storefront carousel.
     *
     * @return list of up to 5 random low-stock products
     */
    List<Product> findLowStockRandom();

    /**
     * Returns all active products ordered by name (A-Z) enriched with rating data.
     *
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findOrderedByNameWithRating();

    /**
     * Returns all active products ordered by price (ascending) enriched with rating data.
     *
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findOrderedByPriceWithRating();

    /**
     * Returns products filtered by active status enriched with rating data.
     *
     * @param active the active status to filter by
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findActiveWithRating(Boolean active);

    /**
     * Returns products of a given category filtered by active status, enriched with rating data.
     *
     * @param categoryId the category to filter by
     * @param active     the active status to filter by
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findActiveByCategoryWithRating(Long categoryId, Boolean active);

    /**
     * Searches products by name (case-insensitive ILIKE) enriched with rating data.
     *
     * @param name the search term
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findByNameWithRating(String name);

    /**
     * Returns products of a given category enriched with rating data.
     *
     * @param categoryId the category to filter by
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findByCategoryWithRating(Long categoryId);

    /**
     * Returns products within a price range enriched with rating data.
     *
     * @param min minimum price (inclusive)
     * @param max maximum price (inclusive)
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findByPriceRangeWithRating(BigDecimal min, BigDecimal max);

    /**
     * Returns up to 5 random featured products enriched with rating data.
     *
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findFeaturedRandomWithRating();

    /**
     * Returns up to 5 random products with active discount enriched with rating data.
     *
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findDealsRandomWithRating();

    /**
     * Returns up to 5 random low-stock products enriched with rating data.
     *
     * @return list of ProductResponse with rating attached
     */
    List<ProductResponse> findLowStockRandomWithRating();

    List<ProductResponse> findAllWithRating();

    /**
     * Returns a product by ID enriched with its aggregated rating data.
     *
     * @param id the product ID
     * @return the product as ProductResponse with rating attached
     */
    ProductResponse findByIdWithRating(Long id);

    /**
     * Returns a paginated list of products enriched with rating data.
     *
     * @param pageable pagination information
     * @return Page of ProductResponse with rating attached
     */
    Page<ProductResponse> findAllPagedWithRating(Pageable pageable);
}
