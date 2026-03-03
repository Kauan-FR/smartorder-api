package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.repository.CategoryRepository;
import com.kauanferreira.smartorder.services.interfaces.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link CategoryService}.
 *
 * <p>Handles business logic for category management,
 * including validation, CRUD operations, and search.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if a category with the same name already exists
     */
    @Override
    @Transactional
    public Category create(Category category) {
        if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()) {
            throw new IllegalArgumentException(String.format("Category with name %s already exists", category.getName()));
        }
        return categoryRepository.save(category);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no category is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(
                String.format("Category with ID %d not found", id)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Category> findAll(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> findAllOrderByNameAsc() {
        return categoryRepository.findAllByOrderByNameAsc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Category> searchByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no category is found with the given ID
     * @throws IllegalArgumentException if a category with the new name already exists
     */
    @Override
    @Transactional
    public Category update(Long id, Category category) {
        Category existing = findById(id);

        if (categoryRepository.findByNameIgnoreCase(category.getName()).isPresent()
        && !existing.getName().equalsIgnoreCase(category.getName())) {
            throw  new IllegalArgumentException(
                    String.format("Category with name %s already exists", category.getName()));
        }
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        return categoryRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no category is found with the given ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Category existing = findById(id);
        categoryRepository.delete(existing);
    }
}
