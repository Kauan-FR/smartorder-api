package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Category;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link CategoryRepository}.
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
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * Clears the database before each test to ensure test isolation.
     */
    @BeforeEach
    public void setUp() {
        categoryRepository.deleteAll();
    }

    @Test
    @Order(1)
    @DisplayName("Should save a category and generate an ID")
    public void shouldSaveCategory() {

        // Arrange
        Category category = new Category(null, "Beverages", "Drinks and refreshments");

        // Act
        Category save = categoryRepository.save(category);

        // Assert
        assertThat(save).isNotNull();
        assertThat(save.getId()).isNotNull();
        assertThat(save.getName()).isEqualTo("Beverages");
        assertThat(save.getDescription()).isEqualTo("Drinks and refreshments");
    }

    @Test
    @Order(2)
    @DisplayName("Should find category by name ignoring case")
    public void shouldFindByNameIgnoreCase() {
        // Arrange
        Category category = new Category(null, "Beverages", "Drinks and refreshments");
        categoryRepository.save(category);

        // Act
        Optional<Category> result = categoryRepository.findByNameIgnoreCase("beverages");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Beverages");
    }

    @Test
    @Order(3)
    @DisplayName("Should return empty when name does not exist")
    public void shouldReturnEmptyWhenNameNotFound() {
        // Act
        Optional<Category> result = categoryRepository.findByNameIgnoreCase("NonExisting");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Should find categories by partial name ignoring case")
    public void shouldFindByNameContainingIgnoreCase() {
        // Arrange
        categoryRepository.save(new Category(null, "Beverages", "Drinks and refreshments"));
        categoryRepository.save(new Category(null, "Snacks", "Light food items"));
        categoryRepository.save(new Category(null, "Desserts", "Sweet treats"));

        // Act
        List<Category> result = categoryRepository.findByNameContainingIgnoreCase("bever");

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result).extracting(Category::getName)
                .containsExactlyInAnyOrder("Beverages");
    }

    @Test
    @Order(5)
    @DisplayName("Should return empty list when no category matches partial name")
    public void shouldReturnEmptyListWhenNoMatchForPartialName() {
        // Arrange
        categoryRepository.save(new Category(null, "Beverages", null));

        // Act
        List<Category> result = categoryRepository.findByNameContainingIgnoreCase("NonExisting");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(6)
    @DisplayName("Should find all categories ordered by name ascending")
    public void shouldFindAllOrderedByNameAsc() {
        // Arrange
        categoryRepository.save(new Category(null, "Snacks", null));
        categoryRepository.save(new Category(null, "Beverages", null));
        categoryRepository.save(new Category(null, "Desserts", null));

        // Act
        List<Category> result = categoryRepository.findAllByOrderByNameAsc();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result).extracting(Category::getName)
                .containsExactly("Beverages", "Desserts", "Snacks");
    }

    @Test
    @Order(7)
    @DisplayName("Should delete a category by ID")
    public void  shouldDeleteCategoryById() {
        // Arrange
        Category save = categoryRepository.save(new Category(null, "Beverages", "Drinks and refreshments"));

        // Act
        categoryRepository.deleteById(save.getId());
        Optional<Category> result = categoryRepository.findById(save.getId());

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(8)
    @DisplayName("Should update a category name")
    public void shouldUpdateCategoryName() {
        // Arrange
        Category save = categoryRepository.save(new Category(null, "Beverages", null));

        // Act
        save.setName("Drinks");
        save.setDescription("Light food items");
        Category updated = categoryRepository.save(save);

        // Assert
        assertThat(updated.getName()).isEqualTo("Drinks");
        assertThat(updated.getDescription()).isEqualTo("Light food items");
        assertThat(updated.getId()).isEqualTo(save.getId());
    }
}
