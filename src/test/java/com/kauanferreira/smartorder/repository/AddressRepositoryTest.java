package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Address;
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
 * Integration tests for {@link AddressRepository}.
 *
 * <p>Validates CRUD operations and custom query methods
 * against the database using Spring Data JPA test slice.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see AddressRepository
 * @see UserRepository
 * @see User
 * @see Address
 * @see Role
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        addressRepository.deleteAll();
        userRepository.deleteAll();

        user1 = userRepository.save(new User(null, "Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null, null, null));
        user2 = userRepository.save(new User(null, "Ana", "ana@email.com", "senha123", Role.CUSTOMER, null, null, null));
    }

    private Address createAddress(String street, String number, String complement,
                                  String city, String state, String zipCode, User user) {
        return new Address(null, street, number, complement, city, state, zipCode, "Brasil", user);
    }

    @Test
    @Order(1)
    @DisplayName("Should save an address and generate an ID")
    void shouldSaveAddress() {
        // Arrange
        Address address = createAddress("Rua das Flores", "123", "Apto 4", "Aracaju", "SE", "49000-000", user1);

        // Act
        Address saved = addressRepository.save(address);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getStreet()).isEqualTo("Rua das Flores");
        assertThat(saved.getNumber()).isEqualTo("123");
        assertThat(saved.getComplement()).isEqualTo("Apto 4");
        assertThat(saved.getCity()).isEqualTo("Aracaju");
        assertThat(saved.getState()).isEqualTo("SE");
        assertThat(saved.getZipCode()).isEqualTo("49000-000");
        assertThat(saved.getCountry()).isEqualTo("Brasil");
        assertThat(saved.getUser().getId()).isEqualTo(user1.getId());
    }

    @Test
    @Order(2)
    @DisplayName("Should save an address without complement")
    void shouldSaveAddressWithoutComplement() {
        // Arrange
        Address address = createAddress("Rua Principal", "S/N", null, "Aracaju", "SE", "49000-001", user1);

        // Act
        Address saved = addressRepository.save(address);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getComplement()).isNull();
    }

    @Test
    @Order(3)
    @DisplayName("Should find all addresses by user ID")
    void shouldFindByUserId() {
        // Arrange
        addressRepository.save(createAddress("Rua A", "100", null, "Aracaju", "SE", "49000-000", user1));
        addressRepository.save(createAddress("Rua B", "200", null, "Salvador", "BA", "40000-000", user1));
        addressRepository.save(createAddress("Rua C", "300", null, "São Paulo", "SP", "01000-000", user2));

        // Act
        List<Address> results = addressRepository.findByUserId(user1.getId());

        // Assert
        assertThat(results).hasSize(2);
        assertThat(results).extracting(Address::getStreet)
                .containsExactlyInAnyOrder("Rua A", "Rua B");
    }

    @Test
    @Order(4)
    @DisplayName("Should return empty list when user has no addresses")
    void shouldReturnEmptyWhenUserHasNoAddresses() {
        // Act
        List<Address> results = addressRepository.findByUserId(user2.getId());

        // Assert
        assertThat(results).isEmpty();
    }

    @Test
    @Order(5)
    @DisplayName("Should find addresses by city ignoring case")
    void shouldFindByCityIgnoreCase() {
        // Arrange
        addressRepository.save(createAddress("Rua A", "100", null, "Aracaju", "SE", "49000-000", user1));
        addressRepository.save(createAddress("Rua B", "200", null, "Aracaju", "SE", "49000-001", user2));
        addressRepository.save(createAddress("Rua C", "300", null, "Salvador", "BA", "40000-000", user1));

        // Act
        List<Address> results = addressRepository.findByCityIgnoreCase("aracaju");

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @Order(6)
    @DisplayName("Should find addresses by state ignoring case")
    void shouldFindByStateIgnoreCase() {
        // Arrange
        addressRepository.save(createAddress("Rua A", "100", null, "Aracaju", "SE", "49000-000", user1));
        addressRepository.save(createAddress("Rua B", "200", null, "Salvador", "BA", "40000-000", user2));

        // Act
        List<Address> results = addressRepository.findByStateIgnoreCase("se");

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getCity()).isEqualTo("Aracaju");
    }

    @Test
    @Order(7)
    @DisplayName("Should find addresses by zip code")
    void shouldFindByZipCode() {
        // Arrange
        addressRepository.save(createAddress("Rua A", "100", null, "Aracaju", "SE", "49000-000", user1));
        addressRepository.save(createAddress("Rua B", "200", null, "Aracaju", "SE", "49000-000", user2));

        // Act
        List<Address> results = addressRepository.findByZipCode("49000-000");

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @Order(8)
    @DisplayName("Should count addresses by user ID")
    void shouldCountByUserId() {
        // Arrange
        addressRepository.save(createAddress("Rua A", "100", null, "Aracaju", "SE", "49000-000", user1));
        addressRepository.save(createAddress("Rua B", "200", null, "Salvador", "BA", "40000-000", user1));

        // Act
        long count = addressRepository.countByUserId(user1.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @Order(9)
    @DisplayName("Should update an address")
    void shouldUpdateAddress() {
        // Arrange
        Address saved = addressRepository.save(createAddress("Rua Antiga", "100", null, "Aracaju", "SE", "49000-000", user1));

        // Act
        saved.setStreet("Rua Nova");
        saved.setNumber("200");
        saved.setComplement("Bloco B");
        Address updated = addressRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getStreet()).isEqualTo("Rua Nova");
        assertThat(updated.getNumber()).isEqualTo("200");
        assertThat(updated.getComplement()).isEqualTo("Bloco B");
    }

    @Test
    @Order(10)
    @DisplayName("Should delete an address by ID")
    void shouldDeleteAddress() {
        // Arrange
        Address saved = addressRepository.save(createAddress("Rua Temp", "999", null, "Aracaju", "SE", "49000-000", user1));

        // Act
        addressRepository.deleteById(saved.getId());
        Optional<Address> result = addressRepository.findById(saved.getId());

        // Assert
        assertThat(result).isEmpty();
    }
}
