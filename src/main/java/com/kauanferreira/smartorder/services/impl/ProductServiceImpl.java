package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.dto.mapper.ProductMapper;
import com.kauanferreira.smartorder.dto.projection.RatingProjection;
import com.kauanferreira.smartorder.dto.response.ProductResponse;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.exception.DuplicateResourceException;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.ProductRepository;
import com.kauanferreira.smartorder.repository.ReviewRepository;
import com.kauanferreira.smartorder.services.interfaces.CategoryService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementation of {@link ProductService}.
 *
 * <p>Handles business logic for product management,
 * including validation, stock control, activation,
 * and category relationship enforcement.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ProductService
 * @see ProductRepository
 * @see CategoryService
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryService categoryService;

    /**
     * {@inheritDoc}
     *
     * @throws DuplicateResourceException if a product with the same name already exists
     * @throws ResourceNotFoundException if the category does not exist
     */
    @Override
    @Transactional
    public Product create(Product product) {
        if (productRepository.findByNameIgnoreCase(product.getName()).isPresent()) {
            throw new DuplicateResourceException(String.format("Product with name %s already exists", product.getName()));
        }
        categoryService.findById(product.getCategory().getId());
        return productRepository.save(product);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Product with ID %d not found", id))
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllOrderByNameAsc() {
        return productRepository.findAllByOrderByNameAsc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllOrderByPriceAsc() {
        return productRepository.findAllByOrderByPriceAsc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> searchByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByCategoryId(Long categoryId) {
        categoryService.findById(categoryId);
        return productRepository.findByCategoryId(categoryId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByActive(Boolean active) {
        return productRepository.findByActive(active);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findActiveByCategoryId(Long categoryId) {
        categoryService.findById(categoryId);
        return productRepository.findByCategoryIdAndActive(categoryId, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPriceBetween(minPrice, maxPrice);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findLowStock() {
        return productRepository.findLowStock();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findFeaturedRandom() {
        return productRepository.findFeaturedRandom(PageRequest.of(0, 5));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findDealsRandom() {
        return productRepository.findDealsRandom(PageRequest.of(0, 5));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Product> findLowStockRandom() {
        return productRepository.findLowStockRandom(PageRequest.of(0, 5));
    }

    @Override
    public List<ProductResponse> findOrderedByNameWithRating() {
        return enrichWithRating(productRepository.findAllByOrderByNameAsc());
    }

    @Override
    public List<ProductResponse> findOrderedByPriceWithRating() {
        return enrichWithRating(productRepository.findAllByOrderByPriceAsc());
    }

    @Override
    public List<ProductResponse> findActiveWithRating(Boolean active) {
        return enrichWithRating(productRepository.findByActive(active));
    }

    @Override
    public List<ProductResponse> findActiveByCategoryWithRating(Long categoryId, Boolean active) {
        return enrichWithRating(productRepository.findByCategoryIdAndActive(categoryId, active));
    }

    @Override
    public List<ProductResponse> findByNameWithRating(String name) {
        return enrichWithRating(productRepository.findByNameContainingIgnoreCase(name));
    }

    @Override
    public List<ProductResponse> findByCategoryWithRating(Long categoryId) {
        return enrichWithRating(productRepository.findByCategoryId(categoryId));
    }

    @Override
    public List<ProductResponse> findByPriceRangeWithRating(BigDecimal min, BigDecimal max) {
        return enrichWithRating(productRepository.findByPriceBetween(min, max));
    }

    @Override
    public List<ProductResponse> findFeaturedRandomWithRating() {
        return enrichWithRating(productRepository.findFeaturedRandom(PageRequest.of(0, 5)));
    }

    @Override
    public List<ProductResponse> findDealsRandomWithRating() {
        return enrichWithRating(productRepository.findDealsRandom(PageRequest.of(0, 5)));
    }

    @Override
    public List<ProductResponse> findLowStockRandomWithRating() {
        return enrichWithRating(productRepository.findLowStockRandom(PageRequest.of(0, 5)));
    }

    @Override
    public List<ProductResponse> findAllWithRating() {
        return enrichWithRating(productRepository.findAll());
    }

    @Override
    public ProductResponse findByIdWithRating(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found. Id: " + id));

        RatingProjection rating = reviewRepository
                .findRatingsByProductIds(List.of(id))
                .stream()
                .findFirst()
                .orElse(null);

        return ProductMapper.toResponseWithRating(product, rating);
    }

    @Override
    public Page<ProductResponse> findAllPagedWithRating(Pageable pageable) {
        Page<Product> page = productRepository.findAll(pageable);

        if (page.isEmpty()) {
            return page.map(p -> null);
        }

        List<Long> ids = page.getContent().stream()
                .map(Product::getId)
                .toList();

        Map<Long, RatingProjection> ratingsMap = reviewRepository
                .findRatingsByProductIds(ids)
                .stream()
                .collect(Collectors.toMap(RatingProjection::productId, Function.identity()));

        return page.map(p -> ProductMapper.toResponseWithRating(p, ratingsMap.get(p.getId())));
    }

    private List<ProductResponse> enrichWithRating(List<Product> products) {
        if (products.isEmpty()) {
            return List.of();
        }

        List<Long> ids = products.stream()
                .map(Product::getId)
                .toList();

        Map<Long, RatingProjection> ratingsMap = reviewRepository
                .findRatingsByProductIds(ids)
                .stream()
                .collect(Collectors.toMap(RatingProjection::productId, Function.identity()));

        return products.stream()
                .map(p -> ProductMapper.toResponseWithRating(p, ratingsMap.get(p.getId())))
                .toList();
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product is found with the given ID
     * @throws ResourceNotFoundException if the category does not exist
     * @throws DuplicateResourceException if a product with the new name already exists
     */
    @Override
    @Transactional
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        categoryService.findById(product.getCategory().getId());

        if (productRepository.findByNameIgnoreCase(product.getName()).isPresent()
                && !existing.getName().equalsIgnoreCase(product.getName())) {
            throw new DuplicateResourceException(
                    String.format("Product with name '%s' already exists", product.getName()));
        }

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setImageUrl(product.getImageUrl());
        existing.setActive(product.getActive());
        existing.setCategory(product.getCategory());
        existing.setDiscountPercent(product.getDiscountPercent());
        existing.setInitialStock(product.getInitialStock());
        existing.setDealExpiresAt(product.getDealExpiresAt());
        existing.setFeatured(product.getFeatured());
        return productRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional
    public Product activate(Long id) {
        Product existing = findById(id);
        existing.setActive(true);
        return productRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional
    public Product deactivate(Long id) {
        Product existing = findById(id);
        existing.setActive(false);
        return productRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Product existing = findById(id);
        productRepository.delete(existing);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findActiveDeals() {
        return productRepository.findActiveDeals(0, LocalDateTime.now());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Product> findFeatured() {
        return productRepository.findByFeaturedTrue();
    }
}
