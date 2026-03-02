package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.*;
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
 * Integration tests for {@link OrderItemRepository}.
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
public class OrderItemRepositoryTest {


    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Order order1;
    private Order order2;
    private Product product1;
    private Product product2;

    @BeforeEach
    void setUp() {
        orderItemRepository.deleteAll();
        orderRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        addressRepository.deleteAll();
        userRepository.deleteAll();

        User user = userRepository.save(new User(null, "Kauan", "kauan@email.com", "senha123", Role.CUSTOMER, null, null));
        Address address = addressRepository.save(new Address(null, "Rua A", "100", null, "Aracaju", "SE", "49000-000", "Brasil", user));
        Category category = categoryRepository.save(new Category(null, "Eletrônicos", "Produtos eletrônicos"));

        product1 = productRepository.save(new Product(null, "Smartphone", null, new BigDecimal("2999.99"), 50, null, true, category));
        product2 = productRepository.save(new Product(null, "Notebook", null, new BigDecimal("4500.00"), 20, null, true, category));

        order1 = orderRepository.save(new Order(null, null, OrderStatus.PENDING, new BigDecimal("0.00"), user, address, new ArrayList<>()));
        order2 = orderRepository.save(new Order(null, null, OrderStatus.CONFIRMED, new BigDecimal("0.00"), user, address, new ArrayList<>()));
    }

    private OrderItem createOrderItem(Integer quantity, BigDecimal price, Order order, Product product) {
        BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
        return new OrderItem(null, quantity, price, subtotal, order, product);
    }

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should save an order item and generate an ID")
    void shouldSaveOrderItem() {
        // Arrange
        OrderItem item = createOrderItem(2, new BigDecimal("2999.99"), order1, product1);

        // Act
        OrderItem saved = orderItemRepository.save(item);

        // Assert
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getQuantity()).isEqualTo(2);
        assertThat(saved.getPrice()).isEqualByComparingTo(new BigDecimal("2999.99"));
        assertThat(saved.getSubtotal()).isEqualByComparingTo(new BigDecimal("5999.98"));
        assertThat(saved.getOrder().getId()).isEqualTo(order1.getId());
        assertThat(saved.getProduct().getId()).isEqualTo(product1.getId());
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should find all items by order ID")
    void shouldFindByOrderId() {
        // Arrange
        orderItemRepository.save(createOrderItem(1, new BigDecimal("2999.99"), order1, product1));
        orderItemRepository.save(createOrderItem(1, new BigDecimal("4500.00"), order1, product2));
        orderItemRepository.save(createOrderItem(2, new BigDecimal("2999.99"), order2, product1));

        // Act
        List<OrderItem> results = orderItemRepository.findByOrderId(order1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("Should find all order items by product ID")
    void shouldFindByProductId() {
        // Arrange
        orderItemRepository.save(createOrderItem(1, new BigDecimal("2999.99"), order1, product1));
        orderItemRepository.save(createOrderItem(2, new BigDecimal("2999.99"), order2, product1));
        orderItemRepository.save(createOrderItem(1, new BigDecimal("4500.00"), order1, product2));

        // Act
        List<OrderItem> results = orderItemRepository.findByProductId(product1.getId());

        // Assert
        assertThat(results).hasSize(2);
    }

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("Should count items by order ID")
    void shouldCountByOrderId() {
        // Arrange
        orderItemRepository.save(createOrderItem(1, new BigDecimal("2999.99"), order1, product1));
        orderItemRepository.save(createOrderItem(1, new BigDecimal("4500.00"), order1, product2));

        // Act
        long count = orderItemRepository.countByOrderId(order1.getId());

        // Assert
        assertThat(count).isEqualTo(2);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("Should update an order item quantity and subtotal")
    void shouldUpdateOrderItem() {
        // Arrange
        OrderItem saved = orderItemRepository.save(createOrderItem(1, new BigDecimal("2999.99"), order1, product1));

        // Act
        saved.setQuantity(3);
        saved.setSubtotal(saved.getPrice().multiply(BigDecimal.valueOf(3)));
        OrderItem updated = orderItemRepository.save(saved);

        // Assert
        assertThat(updated.getId()).isEqualTo(saved.getId());
        assertThat(updated.getQuantity()).isEqualTo(3);
        assertThat(updated.getSubtotal()).isEqualByComparingTo(new BigDecimal("8999.97"));
    }

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("Should delete an order item by ID")
    void shouldDeleteOrderItem() {
        // Arrange
        OrderItem saved = orderItemRepository.save(createOrderItem(1, new BigDecimal("2999.99"), order1, product1));

        // Act
        orderItemRepository.deleteById(saved.getId());
        Optional<OrderItem> result = orderItemRepository.findById(saved.getId());

        // Assert
        assertThat(result).isEmpty();
    }
}
