package com.kauanferreira.smartorder.dto.mapper;

import com.kauanferreira.smartorder.dto.request.ProductRequest;
import com.kauanferreira.smartorder.dto.response.ProductResponse;
import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.entity.Product;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Mapper class for converting between {@link Product} entity
 * and its corresponding DTOs.
 *
 * <p>Provides static methods to convert from request DTOs to entities
 * and from entities to response DTOs. Handles the nested
 * {@link Category} relationship.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ProductRequest
 * @see ProductResponse
 * @see Product
 */
public final class ProductMapper {

    private ProductMapper() {
    }

    /**
     * Converts a {@link ProductRequest} to a {@link Product} entity.
     *
     * <p>Creates a Category reference with only the id set, which is enough
     * for JPA to establish the relationship. The service layer validates
     * that the category exists.</p>
     *
     * @param request the request DTO containing product data
     * @return a new Product entity with fields populated from the request
     */
    public static Product toEntity(ProductRequest request) {
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        product.setActive(request.active() != null ? request.active() : true);

        product.setDiscountPercent(request.discountPercent());
        product.setInitialStock(request.initialStock());
        product.setDealExpiresAt(request.dealExpiresAt());
        product.setFeatured(request.featured() != null ? request.featured() : false);

        Category category = new Category();
        category.setId(request.categoryId());
        product.setCategory(category);

        return product;
    }

    /**
     * Converts a {@link Product} entity to a {@link ProductResponse} DTO.
     *
     * <p>Uses {@link CategoryMapper#toResponse} for the nested category.
     * Calculates the {@code finalPrice} by applying the discount percentage
     * to the original price. If no discount is set, finalPrice equals price.</p>
     *
     * @param product the product entity
     * @return a new ProductResponse with fields populated from the entity
     */
    public static ProductResponse toResponse(Product product) {

        // Calculate final price with discount applied
        BigDecimal finalPrice = product.getPrice();
        if (product.getDiscountPercent() != null && product.getDiscountPercent() > 0) {
            BigDecimal discount = product.getPrice()
                    .multiply(BigDecimal.valueOf(product.getDiscountPercent()))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            finalPrice = product.getPrice().subtract(discount);
        }
        return  new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getActive(),
                product.getDiscountPercent(),
                product.getInitialStock(),
                product.getDealExpiresAt(),
                product.getFeatured(),
                finalPrice,
                CategoryMapper.toResponse(product.getCategory())
        );
    }

    /**
     * Updates an existing {@link Product} entity with data from a {@link ProductRequest}.
     *
     * @param request the request DTO containing updated data
     * @param product the existing entity to update
     */
    public static void updateEntity(ProductRequest request, Product product) {
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        product.setActive(request.active() != null ? request.active() : true);

        product.setDiscountPercent(request.discountPercent());
        product.setInitialStock(request.initialStock());
        product.setDealExpiresAt(request.dealExpiresAt());
        product.setFeatured(request.featured() != null ? request.featured() : false);

        Category category = new Category();
        category.setId(request.categoryId());
        product.setCategory(category);
    }
}
