package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.repository.ProductRepository;
import com.kauanferreira.smartorder.services.impl.ProductServiceImpl;
import com.kauanferreira.smartorder.services.interfaces.CategoryService;
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

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link ProductServiceImpl}.
 *
 * <p>Uses Mockito to mock the repository and service layers,
 * testing product business logic in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ProductServiceImpl
 * @see ProductRepository
 * @see CategoryService
 * @see Category
 * @see Product
 */
@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category electronics;
    private Category clothing;
    private Product smartphone;
    private Product notebook;

    @BeforeEach
    void setUp() {
        electronics = new Category(1L, "Eletrônicos", "Produtos eletrônicos");
        clothing = new Category(2L, "Roupas", "Vestuário em geral");

        smartphone = new Product(1L, "Smartphone", "Celular top",
                new BigDecimal("2999.99"), 50, null, true, electronics);
        notebook = new Product(2L, "Notebook", "Laptop profissional",
                new BigDecimal("4500.00"), 20, null, true, electronics);
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @Order(1)
    @DisplayName("Should create a product successfully")
    void shouldCreateProduct() {
        // Arrange
        Product newProduct = new Product(null, "Smartphone", "Celular top",
                new BigDecimal("2999.99"), 50, null, true, electronics);
        when(productRepository.findByNameIgnoreCase("Smartphone")).thenReturn(Optional.empty());
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.save(any(Product.class))).thenReturn(smartphone);

        // Act
        Product result = productService.create(newProduct);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Smartphone");
        verify(categoryService).findById(1L);
        verify(productRepository).save(newProduct);
    }

    @Test
    @Order(2)
    @DisplayName("Should throw exception when creating product with duplicate name")
    void shouldThrowExceptionWhenCreatingDuplicateName() {
        // Arrange
        Product newProduct = new Product(null, "Smartphone", "Outro celular",
                new BigDecimal("1999.99"), 10, null, true, electronics);
        when(productRepository.findByNameIgnoreCase("Smartphone")).thenReturn(Optional.of(smartphone));

        // Act & Assert
        assertThatThrownBy(() -> productService.create(newProduct))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(productRepository, never()).save(any());
    }

    @Test
    @Order(3)
    @DisplayName("Should throw exception when creating product with non-existent category")
    void shouldThrowExceptionWhenCreatingWithInvalidCategory() {
        // Arrange
        Category fakeCategory = new Category(99L, "Fake", null);
        Product newProduct = new Product(null, "Produto", "Desc",
                new BigDecimal("100.00"), 10, null, true, fakeCategory);
        when(productRepository.findByNameIgnoreCase("Produto")).thenReturn(Optional.empty());
        when(categoryService.findById(99L)).thenThrow(
                new EntityNotFoundException("Category not found. Id: 99"));

        // Act & Assert
        assertThatThrownBy(() -> productService.create(newProduct))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @Order(4)
    @DisplayName("Should find a product by ID")
    void shouldFindById() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));

        // Act
        Product result = productService.findById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Smartphone");
    }

    @Test
    @Order(5)
    @DisplayName("Should throw exception when product ID not found")
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @Order(6)
    @DisplayName("Should find all products")
    void shouldFindAll() {
        // Arrange
        when(productRepository.findAll()).thenReturn(List.of(smartphone, notebook));

        // Act
        List<Product> results = productService.findAll();

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @Order(7)
    @DisplayName("Should return empty list when no products exist")
    void shouldReturnEmptyListWhenNoProducts() {
        // Arrange
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<Product> results = productService.findAll();

        // Assert
        assertThat(results).isEmpty();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @Order(8)
    @DisplayName("Should find all products with pagination")
    void shouldFindAllPaginated() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(smartphone, notebook), pageable, 2);
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<Product> result = productService.findAll(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    // ========================
    // FIND ALL ORDERED
    // ========================

    @Test
    @Order(9)
    @DisplayName("Should find all products ordered by name ascending")
    void shouldFindAllOrderByNameAsc() {
        // Arrange
        when(productRepository.findAllByOrderByNameAsc()).thenReturn(List.of(notebook, smartphone));

        // Act
        List<Product> results = productService.findAllOrderByNameAsc();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("Notebook");
    }

    @Test
    @Order(10)
    @DisplayName("Should find all products ordered by price ascending")
    void shouldFindAllOrderByPriceAsc() {
        // Arrange
        when(productRepository.findAllByOrderByPriceAsc()).thenReturn(List.of(smartphone, notebook));

        // Act
        List<Product> results = productService.findAllOrderByPriceAsc();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getPrice()).isEqualByComparingTo(new BigDecimal("2999.99"));
    }

    // ========================
    // SEARCH BY NAME
    // ========================

    @Test
    @Order(11)
    @DisplayName("Should search products by partial name")
    void shouldSearchByName() {
        // Arrange
        when(productRepository.findByNameContainingIgnoreCase("smart")).thenReturn(List.of(smartphone));

        // Act
        List<Product> results = productService.searchByName("smart");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Smartphone");
    }

    @Test
    @Order(12)
    @DisplayName("Should return empty list when no products match search")
    void shouldReturnEmptyListWhenNoSearchMatch() {
        // Arrange
        when(productRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        // Act
        List<Product> results = productService.searchByName("xyz");

        // Assert
        assertThat(results).isEmpty();
    }

    // ========================
    // FIND BY CATEGORY
    // ========================

    @Test
    @Order(13)
    @DisplayName("Should find products by category ID")
    void shouldFindByCategoryId() {
        // Arrange
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.findByCategoryId(1L)).thenReturn(List.of(smartphone, notebook));

        // Act
        List<Product> results = productService.findByCategoryId(1L);

        // Assert
        assertThat(results).hasSize(2);
        verify(categoryService).findById(1L);
    }

    @Test
    @Order(14)
    @DisplayName("Should throw exception when finding by non-existent category")
    void shouldThrowExceptionWhenFindingByInvalidCategory() {
        // Arrange
        when(categoryService.findById(99L)).thenThrow(
                new EntityNotFoundException("Category not found. Id: 99"));

        // Act & Assert
        assertThatThrownBy(() -> productService.findByCategoryId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ========================
    // FIND BY ACTIVE
    // ========================

    @Test
    @Order(15)
    @DisplayName("Should find active products")
    void shouldFindByActive() {
        // Arrange
        when(productRepository.findByActive(true)).thenReturn(List.of(smartphone, notebook));

        // Act
        List<Product> results = productService.findByActive(true);

        // Assert
        assertThat(results).hasSize(2);
    }

    // ========================
    // FIND ACTIVE BY CATEGORY
    // ========================

    @Test
    @Order(16)
    @DisplayName("Should find active products by category ID")
    void shouldFindActiveByCategoryId() {
        // Arrange
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.findByCategoryIdAndActive(1L, true)).thenReturn(List.of(smartphone));

        // Act
        List<Product> results = productService.findActiveByCategoryId(1L);

        // Assert
        assertThat(results).hasSize(1);
        verify(categoryService).findById(1L);
        verify(productRepository).findByCategoryIdAndActive(1L, true);
    }

    // ========================
    // FIND BY PRICE RANGE
    // ========================

    @Test
    @Order(17)
    @DisplayName("Should find products within price range")
    void shouldFindByPriceRange() {
        // Arrange
        BigDecimal min = new BigDecimal("2000.00");
        BigDecimal max = new BigDecimal("3500.00");
        when(productRepository.findByPriceBetween(min, max)).thenReturn(List.of(smartphone));

        // Act
        List<Product> results = productService.findByPriceRange(min, max);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Smartphone");
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @Order(18)
    @DisplayName("Should update a product successfully")
    void shouldUpdateProduct() {
        // Arrange
        Product updateData = new Product(null, "Smartphone Pro", "Versão atualizada",
                new BigDecimal("3499.99"), 30, "http://img.com/phone.jpg", true, electronics);
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.findByNameIgnoreCase("Smartphone Pro")).thenReturn(Optional.empty());
        when(productRepository.save(any(Product.class))).thenReturn(smartphone);

        // Act
        Product result = productService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(categoryService).findById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @Order(19)
    @DisplayName("Should update product keeping the same name")
    void shouldUpdateProductKeepingSameName() {
        // Arrange
        Product updateData = new Product(null, "Smartphone", "Nova descrição",
                new BigDecimal("2799.99"), 40, null, true, electronics);
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.findByNameIgnoreCase("Smartphone")).thenReturn(Optional.of(smartphone));
        when(productRepository.save(any(Product.class))).thenReturn(smartphone);

        // Act
        Product result = productService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @Order(20)
    @DisplayName("Should throw exception when updating to duplicate name")
    void shouldThrowExceptionWhenUpdatingToDuplicateName() {
        // Arrange
        Product updateData = new Product(null, "Notebook", "Tentando nome existente",
                new BigDecimal("2999.99"), 50, null, true, electronics);
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));
        when(categoryService.findById(1L)).thenReturn(electronics);
        when(productRepository.findByNameIgnoreCase("Notebook")).thenReturn(Optional.of(notebook));

        // Act & Assert
        assertThatThrownBy(() -> productService.update(1L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already exists");

        verify(productRepository, never()).save(any());
    }

    @Test
    @Order(21)
    @DisplayName("Should throw exception when updating non-existent product")
    void shouldThrowExceptionWhenUpdatingNonExistent() {
        // Arrange
        Product updateData = new Product(null, "Novo", "Desc",
                new BigDecimal("100.00"), 10, null, true, electronics);
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.update(99L, updateData))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).save(any());
    }

    // ========================
    // ACTIVATE / DEACTIVATE
    // ========================

    @Test
    @Order(22)
    @DisplayName("Should activate a product")
    void shouldActivateProduct() {
        // Arrange
        Product inactiveProduct = new Product(1L, "Smartphone", null,
                new BigDecimal("2999.99"), 50, null, false, electronics);
        when(productRepository.findById(1L)).thenReturn(Optional.of(inactiveProduct));
        when(productRepository.save(any(Product.class))).thenReturn(inactiveProduct);

        // Act
        Product result = productService.activate(1L);

        // Assert
        assertThat(result.getActive()).isTrue();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @Order(23)
    @DisplayName("Should deactivate a product")
    void shouldDeactivateProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));
        when(productRepository.save(any(Product.class))).thenReturn(smartphone);

        // Act
        Product result = productService.deactivate(1L);

        // Assert
        assertThat(result.getActive()).isFalse();
        verify(productRepository).save(any(Product.class));
    }

    @Test
    @Order(24)
    @DisplayName("Should throw exception when activating non-existent product")
    void shouldThrowExceptionWhenActivatingNonExistent() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.activate(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @Order(25)
    @DisplayName("Should delete a product successfully")
    void shouldDeleteProduct() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(smartphone));
        doNothing().when(productRepository).delete(smartphone);

        // Act
        productService.delete(1L);

        // Assert
        verify(productRepository).delete(smartphone);
    }

    @Test
    @Order(26)
    @DisplayName("Should throw exception when deleting non-existent product")
    void shouldThrowExceptionWhenDeletingNonExistent() {
        // Arrange
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> productService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");

        verify(productRepository, never()).delete(any());
    }
}
