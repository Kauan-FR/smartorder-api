package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.repository.ProductRepository;
import com.kauanferreira.smartorder.services.interfaces.CategoryService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

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

    private final CategoryService categoryService;

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if a product with the same name already exists
     * @throws EntityNotFoundException if the category does not exist
     */
    @Override
    @Transactional
    public Product create(Product product) {
        if (productRepository.findByNameIgnoreCase(product.getName()).isPresent()) {
            throw new IllegalArgumentException(String.format("Product with name %s already exists", product.getName()));
        }
        categoryService.findById(product.getCategory().getId());
        return productRepository.save(product);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Product with ID %d not found", id))
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
     *
     * @throws EntityNotFoundException if no product is found with the given ID
     * @throws EntityNotFoundException if the category does not exist
     * @throws IllegalArgumentException if a product with the new name already exists
     */
    @Override
    @Transactional
    public Product update(Long id, Product product) {
        Product existing = findById(id);
        categoryService.findById(product.getCategory().getId());

        if (productRepository.findByNameIgnoreCase(product.getName()).isPresent()
                && !existing.getName().equalsIgnoreCase(product.getName())) {
            throw new IllegalArgumentException(
                    String.format("Product with name '%s' already exists", product.getName()));
        }

        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setImageUrl(product.getImageUrl());
        existing.setActive(product.getActive());
        existing.setCategory(product.getCategory());
        return productRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no product is found with the given ID
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
     * @throws EntityNotFoundException if no product is found with the given ID
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
     * @throws EntityNotFoundException if no product is found with the given ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Product existing = findById(id);
        productRepository.delete(existing);
    }
}
