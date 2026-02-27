package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link ProductRepository}.
 *
 * <p>Validates CRUD operations and custom query methods
 * against the database using Spring Data JPA test slice.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category electronics;
    private Category clothing;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        electronics = categoryRepository.save(new Category(null, "Electronics", "Electronic devices and gadgets"));
        clothing = categoryRepository.save(new Category(null, "Clothing", "Apparel and accessories"));
    }

    private Product createProduct(String name, String description, BigDecimal price,
                                  Integer stock, Boolean active, Category category) {
        return new Product(null, name, description, price, stock, null,active, category);
    }

    @Test
    @Order(1)
    @DisplayName("Should save a product and generate an ID")
    public void shouldSaveProduct() {

        // Arrange
        Product product = createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics);

        // Act
        Product saved = productRepository.save(product);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Smartphone");
        assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("699.99"));
        assertThat(saved.getStockQuantity()).isEqualTo(50);
        assertThat(saved.getActive()).isTrue();
        assertThat(saved.getCategory().getId()).isEqualTo(electronics.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Should return empty when product name does not exist")
    public void shouldReturnEmptyWhenProductNotFound() {
        //Act
        Optional<Product> result = productRepository.findByNameIgnoreCase("NonExistingProduct");

        //Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(3)
    @DisplayName("Should find products by partial name ignoring case")
    public void shouldFindByNameContainingIgnoreCase() {
        // Arrange
        productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));
        productRepository.save(createProduct("Smartwatch", "Wearable smart device", new BigDecimal("199.99"),
                30, true, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));

        // Act
        List<Product> results = productRepository.findByNameIgnoreCaseContaining("smart");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Product::getName)
                .containsExactlyInAnyOrder("Smartphone", "Smartwatch");
    }

    @Test
    @Order(4)
    @DisplayName("Should find all products by category ID")
    public void shouldFindByCategoryId() {
        // Arrange
        productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));
        productRepository.save(createProduct("T-Shirt", "Cotton t-shirt", new BigDecimal("19.99"),
                200, true, clothing));

        // Act
        List<Product> results = productRepository.findByCategoryId(clothing.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Product::getName)
                .containsExactlyInAnyOrder("Jeans", "T-Shirt");
    }

    @Test
    @Order(5)
    @DisplayName("Should find only active products")
    public void shouldFindByActive() {
        // Arrange
        productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));
        productRepository.save(createProduct("Old Smartphone", "Outdated model smartphone", new BigDecimal("299.99"),
                20, false, electronics));

        // Act
        List<Product> activeProducts = productRepository.findByActive(true);

        // Assert
        assertThat(activeProducts).hasSize(1);
        assertThat(activeProducts.get(0).getName()).isEqualTo("Smartphone");
    }

    @Test
    @Order(6)
    @DisplayName("Should find active products by category")
    public void shouldFindByCategoryIdAndActive() {
        // Arrange
        productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));
        productRepository.save(createProduct("Old Smartphone", "Outdated model smartphone", new BigDecimal("299.99"),
                20, false, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));

        // Act
        List<Product> results = productRepository.findByCategoryIdAndActive(electronics.getId(), true);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Smartphone");
    }

    @Test
    @Order(7)
    @DisplayName("Should find products within a price range")
    public void shouldFindByPriceBetween() {
        // Arrange
        productRepository.save(createProduct("Budget Smartphone", "Affordable smartphone", new BigDecimal("199.99"),
                100, true, electronics));
        productRepository.save(createProduct("Premium Smartphone", "High-end smartphone", new BigDecimal("999.99"),
                20, true, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));

        // Act
        List<Product> results = productRepository.findByPriceBetween(new BigDecimal("100.00"), new BigDecimal("500.00"));

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Budget Smartphone");
    }

    @Test
    @Order(8)
    @DisplayName("Should find all products ordered by price ascending")
    public void shouldFindAllByOrderByPriceAsc() {
        // Arrange
        productRepository.save(createProduct("Budget Smartphone", "Affordable smartphone", new BigDecimal("199.99"),
                100, true, electronics));
        productRepository.save(createProduct("Premium Smartphone", "High-end smartphone", new BigDecimal("999.99"),
                20, true, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));

        // Act
        List<Product> results = productRepository.findAllByOrderByPriceAsc();

        // Assert
        assertThat(results).hasSize(3);
        assertThat(results).extracting(Product::getName)
                .containsExactly("Jeans", "Budget Smartphone", "Premium Smartphone");
    }

    @Test
    @Order(9)
    @DisplayName("Should find all products ordered by name ascending")
    public void shouldFindAllByOrderByNameAsc() {
        // Arrange
        productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));
        productRepository.save(createProduct("Jeans", "Denim jeans", new BigDecimal("49.99"),
                100, true, clothing));
        productRepository.save(createProduct("T-Shirt", "Cotton t-shirt", new BigDecimal("19.99"),
                200, true, clothing));

        // Act
        List<Product> results = productRepository.findAllByOrderByNameAsc();

        // Assert
        assertThat(results).hasSize(3);
        assertThat(results).extracting(Product::getName)
                .containsExactly("Jeans", "Smartphone", "T-Shirt");
    }

    @Test
    @Order(10)
    @DisplayName("Should delete a product by ID")
    public void shouldDeleteProduct() {
        // Arrange
        Product saved = productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));

        // Act
        productRepository.deleteById(saved.getId());
        Optional<Product> result = productRepository.findById(saved.getId());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(11)
    @DisplayName("Should update a product")
    public void shouldUpdateProduct() {
        // Arrange
        Product saved = productRepository.save(createProduct("Smartphone", "Latest model smartphone", new BigDecimal("699.99"),
                50, true, electronics));

        // Act
        saved.setName("Smartphone");
        saved.setPrice(new BigDecimal("649.99"));
        saved.setStockQuantity(40);
        Product updated = productRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getName()).isEqualTo("Smartphone");
        assertThat(updated.getPrice()).isEqualByComparingTo(new BigDecimal("649.99"));
        assertThat(updated.getStockQuantity()).isEqualTo(40);
    }
}
