package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.repository.CategoryRepository;
import com.kauanferreira.smartorder.services.impl.CategoryServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link CategoryServiceImpl}.
 *
 * <p>Uses Mockito to mock the repository layer,
 * testing business logic in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CategoryServiceImpl
 * @see CategoryRepository
 * @see Category
 */
@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category electronics;
    private Category clothing;

    @BeforeEach
    void setUp() {
        electronics = new Category(1L, "Eletrônicos", "Produtos eletrônicos");
        clothing = new Category(2L, "Roupas", "Vestuário em geral");
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @Order(1)
    @DisplayName("Should create a category successfully")
    void shouldCreateCategory() {
        // Arrange
        Category newCategory = new Category(null, "Eletrônicos", "Produtos eletrônicos");
        when(categoryRepository.findByNameIgnoreCase("Eletrônicos")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(electronics);

        // Act
        Category result = categoryService.create(newCategory);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Eletrônicos");
        verify(categoryRepository).findByNameIgnoreCase("Eletrônicos");
        verify(categoryRepository).save(newCategory);
    }

    @Test
    @Order(2)
    @DisplayName("Should throw exception when creating category with duplicate name")
    void shouldThrowExceptionWhenCreatingDuplicateName() {
        // Arrange
        Category newCategory = new Category(null, "Eletrônicos", "Descrição");
        when(categoryRepository.findByNameIgnoreCase("Eletrônicos")).thenReturn(Optional.of(electronics));

        // Act & Assert
        assertThatThrownBy(() -> categoryService.create(newCategory))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(categoryRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @Order(3)
    @DisplayName("Should find a category by ID")
    void shouldFindById() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronics));

        // Act
        Category result = categoryService.findById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Eletrônicos");
        verify(categoryRepository).findById(1L);
    }

    @Test
    @Order(4)
    @DisplayName("Should throw exception when category ID not found")
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(categoryRepository).findById(99L);
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @Order(5)
    @DisplayName("Should find all categories")
    void shouldFindAll() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of(electronics, clothing));

        // Act
        List<Category> results = categoryService.findAll();

        // Assert
        assertThat(results).hasSize(2);
        verify(categoryRepository).findAll();
    }

    @Test
    @Order(6)
    @DisplayName("Should return empty list when no categories exist")
    void shouldReturnEmptyListWhenNoCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Category> results = categoryService.findAll();

        // Assert
        assertThat(results).isEmpty();
        verify(categoryRepository).findAll();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @Order(7)
    @DisplayName("Should find all categories with pagination")
    void shouldFindAllPaginated() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> page = new PageImpl<>(List.of(electronics, clothing), pageable, 2);
        when(categoryRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Category> result = categoryService.findAll(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(categoryRepository).findAll(pageable);
    }

    // ========================
    // FIND ALL ORDERED BY NAME
    // ========================

    @Test
    @Order(8)
    @DisplayName("Should find all categories ordered by name ascending")
    void shouldFindAllOrderByNameAsc() {
        // Arrange
        when(categoryRepository.findAllByOrderByNameAsc()).thenReturn(List.of(electronics, clothing));

        // Act
        List<Category> results = categoryService.findAllOrderByNameAsc();

        // Assert
        assertThat(results).hasSize(2);
        verify(categoryRepository).findAllByOrderByNameAsc();
    }

    // ========================
    // SEARCH BY NAME
    // ========================

    @Test
    @Order(9)
    @DisplayName("Should search categories by partial name")
    void shouldSearchByName() {
        // Arrange
        when(categoryRepository.findByNameContainingIgnoreCase("elet")).thenReturn(List.of(electronics));

        // Act
        List<Category> results = categoryService.searchByName("elet");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Eletrônicos");
        verify(categoryRepository).findByNameContainingIgnoreCase("elet");
    }

    @Test
    @Order(10)
    @DisplayName("Should return empty list when no categories match search")
    void shouldReturnEmptyListWhenNoSearchMatch() {
        // Arrange
        when(categoryRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        // Act
        List<Category> results = categoryService.searchByName("xyz");

        // Assert
        assertThat(results).isEmpty();
        verify(categoryRepository).findByNameContainingIgnoreCase("xyz");
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @Order(11)
    @DisplayName("Should update a category successfully")
    void shouldUpdateCategory() {
        // Arrange
        Category updateData = new Category(null, "Eletrônicos Atualizados", "Nova descrição");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(categoryRepository.findByNameIgnoreCase("Eletrônicos Atualizados")).thenReturn(Optional.empty());
        when(categoryRepository.save(any(Category.class))).thenReturn(electronics);

        // Act
        Category result = categoryService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @Order(12)
    @DisplayName("Should update category keeping the same name")
    void shouldUpdateCategoryKeepingSameName() {
        // Arrange
        Category updateData = new Category(null, "Eletrônicos", "Descrição atualizada");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(categoryRepository.findByNameIgnoreCase("Eletrônicos")).thenReturn(Optional.of(electronics));
        when(categoryRepository.save(any(Category.class))).thenReturn(electronics);

        // Act
        Category result = categoryService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    @Order(13)
    @DisplayName("Should throw exception when updating to a duplicate name")
    void shouldThrowExceptionWhenUpdatingToDuplicateName() {
        // Arrange
        Category updateData = new Category(null, "Roupas", "Tentando usar nome existente");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronics));
        when(categoryRepository.findByNameIgnoreCase("Roupas")).thenReturn(Optional.of(clothing));

        // Act & Assert
        assertThatThrownBy(() -> categoryService.update(1L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(categoryRepository, never()).save(any());
    }

    @Test
    @Order(14)
    @DisplayName("Should throw exception when updating non-existent category")
    void shouldThrowExceptionWhenUpdatingNonExistent() {
        // Arrange
        Category updateData = new Category(null, "Nova", "Descrição");
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.update(99L, updateData))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(categoryRepository, never()).save(any());
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @Order(15)
    @DisplayName("Should delete a category successfully")
    void shouldDeleteCategory() {
        // Arrange
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(electronics));
        doNothing().when(categoryRepository).delete(electronics);

        // Act
        categoryService.delete(1L);

        // Assert
        verify(categoryRepository).findById(1L);
        verify(categoryRepository).delete(electronics);
    }

    @Test
    @Order(16)
    @DisplayName("Should throw exception when deleting non-existent category")
    void shouldThrowExceptionWhenDeletingNonExistent() {
        // Arrange
        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> categoryService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(categoryRepository, never()).delete(any());
    }
}
