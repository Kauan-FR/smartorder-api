package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for {@link OrderRepository}.
 *
 * <p>Validates CRUD operations and custom query methods
 * against the database using Spring Data JPA test slice.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderRepository
 * @see UserRepository
 * @see AddressRepository
 * @see User
 * @see Address
 * @see Order
 * @see OrderStatus
 * @see Role
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private User user1;
    private User user2;
    private Address address1;
    private Address address2;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();

        user1 = userRepository.save(new User(null, "Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null, null, null));
        user2 = userRepository.save(new User(null, "Ana", "ana@email.com", "senha123", Role.CUSTOMER, null, null, null));

        address1 = addressRepository.save(new Address(null, "Rua A", "100", null, "Aracaju", "SE", "49000-000", "Brasil", user1));
        address2 = addressRepository.save(new Address(null, "Rua B", "200", null, "Salvador", "BA", "40000-000", "Brasil", user2));
    }

    private Order createOrder(OrderStatus status, BigDecimal total, User user, Address address) {
        return new Order(null, null, status, total, user, address, new ArrayList<>());
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should save an order and generate an ID")
    void shouldSaveOrder() {
        // Arrange
        Order order = createOrder(OrderStatus.PENDING, new BigDecimal("299.90"), user1, address1);

        // Act
        Order saved = orderRepository.save(order);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getOrderDate()).isNotNull();
        assertThat(saved.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(saved.getTotalAmount()).isEqualByComparingTo(new BigDecimal("299.90"));
        assertThat(saved.getUser().getId()).isEqualTo(user1.getId());
        assertThat(saved.getAddress().getId()).isEqualTo(address1.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should find orders by user ID ordered by date descending")
    void shouldFindByUserIdOrderByOrderDateDesc() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.DELIVERED, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("200.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("300.00"), user2, address2));

        // Act
        List<Order> results = orderRepository.findByUserIdOrderByOrderDateDesc(user1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("Should find orders by status")
    void shouldFindByStatus() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("200.00"), user2, address2));
        orderRepository.save(createOrder(OrderStatus.DELIVERED, new BigDecimal("300.00"), user1, address1));

        // Act
        List<Order> results = orderRepository.findByStatus(OrderStatus.PENDING);

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("Should find orders by user ID and status")
    void shouldFindByUserIdAndStatus() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.DELIVERED, new BigDecimal("200.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("300.00"), user2, address2));

        // Act
        List<Order> results = orderRepository.findByUserIdAndStatus(user1.getId(), OrderStatus.PENDING);

        // Assert
        assertThat(results).hasSize(1);
        assertThat(results.getFirst().getTotalAmount()).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("Should find orders by address ID")
    void shouldFindByAddressId() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.CONFIRMED, new BigDecimal("200.00"), user1, address1));

        // Act
        List<Order> results = orderRepository.findByAddressId(address1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("Should count orders by user ID")
    void shouldCountByUserId() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.DELIVERED, new BigDecimal("200.00"), user1, address1));

        // Act
        long count = orderRepository.countByUserId(user1.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("Should count orders by status")
    void shouldCountByStatus() {
        // Arrange
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));
        orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("200.00"), user2, address2));
        orderRepository.save(createOrder(OrderStatus.DELIVERED, new BigDecimal("300.00"), user1, address1));

        // Act
        long count = orderRepository.countByStatus(OrderStatus.PENDING);

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.Order(8)
    @DisplayName("Should update order status")
    void shouldUpdateOrderStatus() {
        // Arrange
        Order saved = orderRepository.save(createOrder(OrderStatus.PENDING, new BigDecimal("100.00"), user1, address1));

        // Act
        saved.setStatus(OrderStatus.CONFIRMED);
        Order updated = orderRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
    }

    @Test
    @org.junit.jupiter.api.Order(9)
    @DisplayName("Should delete an order by ID")
    void shouldDeleteOrder() {
        // Arrange
        Order saved = orderRepository.save(createOrder(OrderStatus.CANCELLED, new BigDecimal("50.00"), user1, address1));

        // Act
        orderRepository.deleteById(saved.getId());
        Optional<Order> result = orderRepository.findById(saved.getId());

        // Assert
        assertThat(result).isEmpty();
    }
}
