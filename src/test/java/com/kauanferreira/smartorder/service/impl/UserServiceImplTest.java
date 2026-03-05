package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.impl.UserServiceImpl;
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
 * Unit tests for {@link UserServiceImpl}.
 *
 * <p>Uses Mockito to mock the repository layer,
 * testing user business logic in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see UserServiceImpl
 * @see UserRepository
 * @see User
 * @see Role
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User kauan;
    private User ana;

    @BeforeEach
    void setUp() {
        kauan = new User(1L, "Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, "(79) 99999-0000", null);
        ana = new User(2L, "Ana", "ana@email.com", "senha123", Role.CUSTOMER, null, null);
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @Order(1)
    @DisplayName("Should create a user successfully")
    void shouldCreateUser() {
        // Arrange
        User newUser = new User(null, "Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null, null);
        when(userRepository.existsByEmailIgnoreCase("kauan@email.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(kauan);

        // Act
        User result = userService.create(newUser);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Kauan");
        assertThat(result.getEmail()).isEqualTo("kauan@email.com");
        verify(userRepository).existsByEmailIgnoreCase("kauan@email.com");
        verify(userRepository).save(newUser);
    }

    @Test
    @Order(2)
    @DisplayName("Should throw exception when creating user with duplicate email")
    void shouldThrowExceptionWhenCreatingDuplicateEmail() {
        // Arrange
        User newUser = new User(null, "Outro", "kauan@email.com", "senha123", Role.CUSTOMER, null, null);
        when(userRepository.existsByEmailIgnoreCase("kauan@email.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.create(newUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already registered");

        verify(userRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @Order(3)
    @DisplayName("Should find a user by ID")
    void shouldFindById() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));

        // Act
        User result = userService.findById(1L);

        // Assert
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Kauan");
    }

    @Test
    @Order(4)
    @DisplayName("Should throw exception when user ID not found")
    void shouldThrowExceptionWhenIdNotFound() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // ========================
    // FIND BY EMAIL
    // ========================

    @Test
    @Order(5)
    @DisplayName("Should find a user by email")
    void shouldFindByEmail() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase("kauan@email.com")).thenReturn(Optional.of(kauan));

        // Act
        User result = userService.findByEmail("kauan@email.com");

        // Assert
        assertThat(result.getEmail()).isEqualTo("kauan@email.com");
        assertThat(result.getName()).isEqualTo("Kauan");
    }

    @Test
    @Order(6)
    @DisplayName("Should throw exception when email not found")
    void shouldThrowExceptionWhenEmailNotFound() {
        // Arrange
        when(userRepository.findByEmailIgnoreCase("naoexiste@email.com")).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.findByEmail("naoexiste@email.com"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("naoexiste@email.com");
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @Order(7)
    @DisplayName("Should find all users")
    void shouldFindAll() {
        // Arrange
        when(userRepository.findAll()).thenReturn(List.of(kauan, ana));

        // Act
        List<User> results = userService.findAll();

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @Order(8)
    @DisplayName("Should return empty list when no users exist")
    void shouldReturnEmptyListWhenNoUsers() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<User> results = userService.findAll();

        // Assert
        assertThat(results).isEmpty();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @Order(9)
    @DisplayName("Should find all users with pagination")
    void shouldFindAllPaginated() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<User> page = new PageImpl<>(List.of(kauan, ana), pageable, 2);
        when(userRepository.findAll(pageable)).thenReturn(page);

        // Act
        Page<User> result = userService.findAll(pageable);

        // Assert
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
    }

    // ========================
    // FIND ALL ORDERED BY NAME
    // ========================

    @Test
    @Order(10)
    @DisplayName("Should find all users ordered by name ascending")
    void shouldFindAllOrderByNameAsc() {
        // Arrange
        when(userRepository.findAllByOrderByNameAsc()).thenReturn(List.of(ana, kauan));

        // Act
        List<User> results = userService.findAllOrderByNameAsc();

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getName()).isEqualTo("Ana");
    }

    // ========================
    // SEARCH BY NAME
    // ========================

    @Test
    @Order(11)
    @DisplayName("Should search users by partial name")
    void shouldSearchByName() {
        // Arrange
        when(userRepository.findByNameContainingIgnoreCase("kau")).thenReturn(List.of(kauan));

        // Act
        List<User> results = userService.searchByName("kau");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getName()).isEqualTo("Kauan");
    }

    @Test
    @Order(12)
    @DisplayName("Should return empty list when no users match search")
    void shouldReturnEmptyListWhenNoSearchMatch() {
        // Arrange
        when(userRepository.findByNameContainingIgnoreCase("xyz")).thenReturn(Collections.emptyList());

        // Act
        List<User> results = userService.searchByName("xyz");

        // Assert
        assertThat(results).isEmpty();
    }

    // ========================
    // FIND BY ROLE
    // ========================

    @Test
    @Order(13)
    @DisplayName("Should find users by role")
    void shouldFindByRole() {
        // Arrange
        when(userRepository.findByRole(Role.CUSTOMER)).thenReturn(List.of(kauan, ana));

        // Act
        List<User> results = userService.findByRole(Role.CUSTOMER);

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @Order(14)
    @DisplayName("Should return empty list when no users with given role")
    void shouldReturnEmptyListWhenNoUsersWithRole() {
        // Arrange
        when(userRepository.findByRole(Role.ADMIN)).thenReturn(Collections.emptyList());

        // Act
        List<User> results = userService.findByRole(Role.ADMIN);

        // Assert
        assertThat(results).isEmpty();
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @Order(15)
    @DisplayName("Should update a user successfully")
    void shouldUpdateUser() {
        // Arrange
        User updateData = new User(null, "Kauan Ferreira", "kauan@email.com", null, Role.CUSTOMER, "(79) 88888-0000", null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));
        when(userRepository.save(any(User.class))).thenReturn(kauan);

        // Act
        User result = userService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Order(16)
    @DisplayName("Should update user with new email successfully")
    void shouldUpdateUserWithNewEmail() {
        // Arrange
        User updateData = new User(null, "Kauan", "novoemail@email.com", null, Role.CUSTOMER, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));
        when(userRepository.existsByEmailIgnoreCase("novoemail@email.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(kauan);

        // Act
        User result = userService.update(1L, updateData);

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).existsByEmailIgnoreCase("novoemail@email.com");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Order(17)
    @DisplayName("Should throw exception when updating to duplicate email")
    void shouldThrowExceptionWhenUpdatingToDuplicateEmail() {
        // Arrange
        User updateData = new User(null, "Kauan", "ana@email.com", null, Role.CUSTOMER, null, null);
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));
        when(userRepository.existsByEmailIgnoreCase("ana@email.com")).thenReturn(true);

        // Act & Assert
        assertThatThrownBy(() -> userService.update(1L, updateData))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already registered");

        verify(userRepository, never()).save(any());
    }

    @Test
    @Order(18)
    @DisplayName("Should throw exception when updating non-existent user")
    void shouldThrowExceptionWhenUpdatingNonExistent() {
        // Arrange
        User updateData = new User(null, "Novo", "novo@email.com", null, Role.CUSTOMER, null, null);
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.update(99L, updateData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(userRepository, never()).save(any());
    }

    // ========================
    // UPDATE PASSWORD
    // ========================

    @Test
    @Order(19)
    @DisplayName("Should update user password successfully")
    void shouldUpdatePassword() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));
        when(userRepository.save(any(User.class))).thenReturn(kauan);

        // Act
        User result = userService.updatePassword(1L, "novaSenha123");

        // Assert
        assertThat(result).isNotNull();
        verify(userRepository).save(any(User.class));
    }

    @Test
    @Order(20)
    @DisplayName("Should throw exception when updating password for non-existent user")
    void shouldThrowExceptionWhenUpdatingPasswordNonExistent() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.updatePassword(99L, "senha"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(userRepository, never()).save(any());
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @Order(21)
    @DisplayName("Should delete a user successfully")
    void shouldDeleteUser() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(kauan));
        doNothing().when(userRepository).delete(kauan);

        // Act
        userService.delete(1L);

        // Assert
        verify(userRepository).delete(kauan);
    }

    @Test
    @Order(22)
    @DisplayName("Should throw exception when deleting non-existent user")
    void shouldThrowExceptionWhenDeletingNonExistent() {
        // Arrange
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> userService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");

        verify(userRepository, never()).delete(any());
    }
}
