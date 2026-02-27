package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
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
 * Integration tests for {@link UserRepository}.
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
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    private User createUser(String name, String email, String password, Role role, String phone) {

        return new User(null, name, email, password, role, phone, null);
    }

    @Test
    @Order(1)
    @DisplayName("Should save a user and generate an ID")
    void shouldSaveUser() {
        // Arrange
        User user = createUser("Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, "(79) 99999-0000");

        // Act
        User saved = userRepository.save(user);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Kauan");
        assertThat(saved.getEmail()).isEqualTo("kauan@email.com");
        assertThat(saved.getRole()).isEqualTo(Role.CUSTOMER);
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @Order(2)
    @DisplayName("Should find a user by email ignoring case")
    void shouldFindByEmailIgnoreCase() {
        // Arrange
        userRepository.save(createUser("Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        Optional<User> result = userRepository.findByEmailIgnoreCase("KAUAN@EMAIL.COM");

        // Assert
        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Kauan");
    }

    @Test
    @Order(3)
    @DisplayName("Should return empty when email does not exist")
    void shouldReturnEmptyWhenEmailNotFound() {
        // Act
        Optional<User> result = userRepository.findByEmailIgnoreCase("naoexiste@email.com");

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    @Order(4)
    @DisplayName("Should return true when email already exists")
    void shouldReturnTrueWhenEmailExists() {
        // Arrange
        userRepository.save(createUser("Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        boolean exists = userRepository.existsByEmailIgnoreCase("kauan@email.com");

        // Assert
        assertThat(exists).isTrue();
    }

    @Test
    @Order(5)
    @DisplayName("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        // Act
        boolean exists = userRepository.existsByEmailIgnoreCase("naoexiste@email.com");

        // Assert
        assertThat(exists).isFalse();
    }

    @Test
    @Order(6)
    @DisplayName("Should find users by role")
    void shouldFindByRole() {
        // Arrange
        userRepository.save(createUser("Admin", "admin@email.com", "senha123", Role.ADMIN, null));
        userRepository.save(createUser("Cliente 1", "cliente1@email.com", "senha123", Role.CUSTOMER, null));
        userRepository.save(createUser("Cliente 2", "cliente2@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        List<User> admins = userRepository.findByRole(Role.ADMIN);
        List<User> customers = userRepository.findByRole(Role.CUSTOMER);

        // Assert
        assertThat(admins).hasSize(1);
        assertThat(admins.get(0).getName()).isEqualTo("Admin");
        assertThat(customers).hasSize(2);
    }

    @Test
    @Order(7)
    @DisplayName("Should find users by partial name ignoring case")
    void shouldFindByNameContainingIgnoreCase() {
        // Arrange
        userRepository.save(createUser("Kauan Ferreira", "kauan@email.com", "senha123", Role.CUSTOMER, null));
        userRepository.save(createUser("Ana Ferreira", "ana@email.com", "senha123", Role.CUSTOMER, null));
        userRepository.save(createUser("João Silva", "joao@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        List<User> results = userRepository.findByNameContainingIgnoreCase("ferreira");

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(User::getName)
                .containsExactlyInAnyOrder("Kauan Ferreira", "Ana Ferreira");
    }

    @Test
    @Order(8)
    @DisplayName("Should find all users ordered by name ascending")
    void shouldFindAllByOrderByNameAsc() {
        // Arrange
        userRepository.save(createUser("Zara", "zara@email.com", "senha123", Role.CUSTOMER, null));
        userRepository.save(createUser("Ana", "ana@email.com", "senha123", Role.CUSTOMER, null));
        userRepository.save(createUser("Maria", "maria@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        List<User> results = userRepository.findAllByOrderByNameAsc();

        // Assert
        assertThat(results).hasSize(3);
        assertThat(results).extracting(User::getName)
                .containsExactly("Ana", "Maria", "Zara");
    }

    @Test
    @Order(9)
    @DisplayName("Should update a user")
    void shouldUpdateUser() {
        // Arrange
        User saved = userRepository.save(createUser("Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        saved.setName("Kauan Ferreira");
        saved.setPhone("(79) 99999-0000");
        User updated = userRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getName()).isEqualTo("Kauan Ferreira");
        assertThat(updated.getPhone()).isEqualTo("(79) 99999-0000");
    }

    @Test
    @Order(10)
    @DisplayName("Should delete a user by ID")
    void shouldDeleteUser() {
        // Arrange
        User saved = userRepository.save(createUser("Temporário", "temp@email.com", "senha123", Role.CUSTOMER, null));

        // Act
        userRepository.deleteById(saved.getId());
        Optional<User> result = userRepository.findById(saved.getId());

        // Assert
        assertThat(result).isEmpty();
    }
}
