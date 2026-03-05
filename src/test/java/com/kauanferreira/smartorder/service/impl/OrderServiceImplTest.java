package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.repository.OrderRepository;
import com.kauanferreira.smartorder.services.impl.OrderServiceImpl;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link OrderServiceImpl}.
 *
 * <p>Uses Mockito to mock {@link OrderRepository}, {@link UserService},
 * and {@link AddressService}, testing all CRUD operations and query methods
 * in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderServiceImpl
 * @see OrderRepository
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceImplTest {


    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserService userService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User user;
    private Address address1;
    private Address address2;
    private com.kauanferreira.smartorder.entity.Order order1;
    private com.kauanferreira.smartorder.entity.Order order2;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@email.com");
        user.setPassword("password123");
        user.setRole(Role.CUSTOMER);

        address1 = new Address();
        address1.setId(1L);
        address1.setStreet("Rua A");
        address1.setNumber("100");
        address1.setCity("Aracaju");
        address1.setState("Sergipe");
        address1.setZipCode("49000-000");
        address1.setCountry("Brasil");
        address1.setUser(user);

        address2 = new Address();
        address2.setId(2L);
        address2.setStreet("Rua B");
        address2.setNumber("200");
        address2.setCity("Salvador");
        address2.setState("Bahia");
        address2.setZipCode("40000-000");
        address2.setCountry("Brasil");
        address2.setUser(user);

        order1 = new com.kauanferreira.smartorder.entity.Order();
        order1.setId(1L);
        order1.setStatus(OrderStatus.PENDING);
        order1.setTotalAmount(new BigDecimal("150.00"));
        order1.setUser(user);
        order1.setAddress(address1);

        order2 = new com.kauanferreira.smartorder.entity.Order();
        order2.setId(2L);
        order2.setStatus(OrderStatus.CONFIRMED);
        order2.setTotalAmount(new BigDecimal("300.00"));
        order2.setUser(user);
        order2.setAddress(address2);
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should create order successfully")
    void shouldCreateOrder() {
        when(userService.findById(1L)).thenReturn(user);
        when(addressService.findById(1L)).thenReturn(address1);
        when(orderRepository.save(order1)).thenReturn(order1);

        com.kauanferreira.smartorder.entity.Order result = orderService.create(order1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        assertThat(result.getTotalAmount()).isEqualByComparingTo(new BigDecimal("150.00"));
        assertThat(result.getUser().getId()).isEqualTo(1L);
        assertThat(result.getAddress().getId()).isEqualTo(1L);
        verify(userService).findById(1L);
        verify(addressService).findById(1L);
        verify(orderRepository).save(order1);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should throw exception when creating order with non-existent user")
    void shouldThrowExceptionWhenCreatingOrderWithNonExistentUser() {
        when(userService.findById(1L)).thenThrow(
                new EntityNotFoundException("User not found. Id: 1"));

        assertThatThrownBy(() -> orderService.create(order1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userService).findById(1L);
        verify(addressService, never()).findById(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("Should throw exception when creating order with non-existent address")
    void shouldThrowExceptionWhenCreatingOrderWithNonExistentAddress() {
        when(userService.findById(1L)).thenReturn(user);
        when(addressService.findById(1L)).thenThrow(
                new EntityNotFoundException("Address not found. Id: 1"));

        assertThatThrownBy(() -> orderService.create(order1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Address not found");

        verify(userService).findById(1L);
        verify(addressService).findById(1L);
        verify(orderRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("Should find order by id successfully")
    void shouldFindOrderById() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        com.kauanferreira.smartorder.entity.Order result = orderService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository).findById(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("Should throw exception when order not found by id")
    void shouldThrowExceptionWhenOrderNotFoundById() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.findById(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 99 not found");

        verify(orderRepository).findById(99L);
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("Should return all orders")
    void shouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order1, order2));

        List<Order> result = orderService.findAll();

        assertThat(result).hasSize(2);
        verify(orderRepository).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("Should return empty list when no orders exist")
    void shouldReturnEmptyListWhenNoOrdersExist() {
        when(orderRepository.findAll()).thenReturn(Collections.emptyList());

        List<com.kauanferreira.smartorder.entity.Order> result = orderService.findAll();

        assertThat(result).isEmpty();
        verify(orderRepository).findAll();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @org.junit.jupiter.api.Order(8)
    @DisplayName("Should return orders with pagination")
    void shouldReturnOrdersWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<com.kauanferreira.smartorder.entity.Order> page =
                new PageImpl<>(List.of(order1, order2), pageable, 2);
        when(orderRepository.findAll(pageable)).thenReturn(page);

        Page<Order> result = orderService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(orderRepository).findAll(pageable);
    }

    // ========================
    // FIND BY USER ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(9)
    @DisplayName("Should return orders by user id")
    void shouldReturnOrdersByUserId() {
        when(userService.findById(1L)).thenReturn(user);
        when(orderRepository.findByUserIdOrderByOrderDateDesc(1L))
                .thenReturn(List.of(order1, order2));

        List<com.kauanferreira.smartorder.entity.Order> result = orderService.findByUserId(1L);

        assertThat(result).hasSize(2);
        verify(userService).findById(1L);
        verify(orderRepository).findByUserIdOrderByOrderDateDesc(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    @DisplayName("Should return empty list when user has no orders")
    void shouldReturnEmptyListWhenUserHasNoOrders() {
        when(userService.findById(1L)).thenReturn(user);
        when(orderRepository.findByUserIdOrderByOrderDateDesc(1L))
                .thenReturn(Collections.emptyList());

        List<com.kauanferreira.smartorder.entity.Order> result = orderService.findByUserId(1L);

        assertThat(result).isEmpty();
        verify(userService).findById(1L);
        verify(orderRepository).findByUserIdOrderByOrderDateDesc(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    @DisplayName("Should throw exception when finding orders by non-existent user id")
    void shouldThrowExceptionWhenFindingOrdersByNonExistentUserId() {
        when(userService.findById(99L)).thenThrow(
                new EntityNotFoundException("User not found. Id: 99"));

        assertThatThrownBy(() -> orderService.findByUserId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userService).findById(99L);
        verify(orderRepository, never()).findByUserIdOrderByOrderDateDesc(any());
    }

    // ========================
    // FIND BY STATUS
    // ========================

    @Test
    @org.junit.jupiter.api.Order(12)
    @DisplayName("Should return orders by status")
    void shouldReturnOrdersByStatus() {
        when(orderRepository.findByStatus(OrderStatus.PENDING)).thenReturn(List.of(order1));

        List<com.kauanferreira.smartorder.entity.Order> result =
                orderService.findByStatus(OrderStatus.PENDING);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(OrderStatus.PENDING);
        verify(orderRepository).findByStatus(OrderStatus.PENDING);
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    @DisplayName("Should return empty list when no orders found by status")
    void shouldReturnEmptyListWhenNoOrdersFoundByStatus() {
        when(orderRepository.findByStatus(OrderStatus.CANCELLED))
                .thenReturn(Collections.emptyList());

        List<com.kauanferreira.smartorder.entity.Order> result =
                orderService.findByStatus(OrderStatus.CANCELLED);

        assertThat(result).isEmpty();
        verify(orderRepository).findByStatus(OrderStatus.CANCELLED);
    }

    // ========================
    // FIND BY USER ID AND STATUS
    // ========================

    @Test
    @org.junit.jupiter.api.Order(14)
    @DisplayName("Should return orders by user id and status")
    void shouldReturnOrdersByUserIdAndStatus() {
        when(userService.findById(1L)).thenReturn(user);
        when(orderRepository.findByUserIdAndStatus(1L, OrderStatus.PENDING))
                .thenReturn(List.of(order1));

        List<com.kauanferreira.smartorder.entity.Order> result =
                orderService.findByUserIdAndStatus(1L, OrderStatus.PENDING);

        assertThat(result).hasSize(1);
        verify(userService).findById(1L);
        verify(orderRepository).findByUserIdAndStatus(1L, OrderStatus.PENDING);
    }

    @Test
    @org.junit.jupiter.api.Order(15)
    @DisplayName("Should throw exception when finding by non-existent user id and status")
    void shouldThrowExceptionWhenFindingByNonExistentUserIdAndStatus() {
        when(userService.findById(99L)).thenThrow(
                new EntityNotFoundException("User not found. Id: 99"));

        assertThatThrownBy(() -> orderService.findByUserIdAndStatus(99L, OrderStatus.PENDING))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");

        verify(userService).findById(99L);
        verify(orderRepository, never()).findByUserIdAndStatus(any(), any());
    }

    // ========================
    // FIND BY ADDRESS ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(16)
    @DisplayName("Should return orders by address id")
    void shouldReturnOrdersByAddressId() {
        when(orderRepository.findByAddressId(1L)).thenReturn(List.of(order1));

        List<com.kauanferreira.smartorder.entity.Order> result =
                orderService.findByAddressId(1L);

        assertThat(result).hasSize(1);
        verify(orderRepository).findByAddressId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(17)
    @DisplayName("Should return empty list when no orders found by address id")
    void shouldReturnEmptyListWhenNoOrdersFoundByAddressId() {
        when(orderRepository.findByAddressId(99L)).thenReturn(Collections.emptyList());

        List<com.kauanferreira.smartorder.entity.Order> result =
                orderService.findByAddressId(99L);

        assertThat(result).isEmpty();
        verify(orderRepository).findByAddressId(99L);
    }

    // ========================
    // COUNT BY USER ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(18)
    @DisplayName("Should count orders by user id")
    void shouldCountOrdersByUserId() {
        when(orderRepository.countByUserId(1L)).thenReturn(2L);

        long result = orderService.countByUserId(1L);

        assertThat(result).isEqualTo(2L);
        verify(orderRepository).countByUserId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(19)
    @DisplayName("Should return zero when user has no orders")
    void shouldReturnZeroWhenUserHasNoOrders() {
        when(orderRepository.countByUserId(1L)).thenReturn(0L);

        long result = orderService.countByUserId(1L);

        assertThat(result).isZero();
        verify(orderRepository).countByUserId(1L);
    }

    // ========================
    // COUNT BY STATUS
    // ========================

    @Test
    @org.junit.jupiter.api.Order(20)
    @DisplayName("Should count orders by status")
    void shouldCountOrdersByStatus() {
        when(orderRepository.countByStatus(OrderStatus.PENDING)).thenReturn(5L);

        long result = orderService.countByStatus(OrderStatus.PENDING);

        assertThat(result).isEqualTo(5L);
        verify(orderRepository).countByStatus(OrderStatus.PENDING);
    }

    @Test
    @org.junit.jupiter.api.Order(21)
    @DisplayName("Should return zero when no orders with given status")
    void shouldReturnZeroWhenNoOrdersWithGivenStatus() {
        when(orderRepository.countByStatus(OrderStatus.CANCELLED)).thenReturn(0L);

        long result = orderService.countByStatus(OrderStatus.CANCELLED);

        assertThat(result).isZero();
        verify(orderRepository).countByStatus(OrderStatus.CANCELLED);
    }

    // ========================
    // UPDATE STATUS
    // ========================

    @Test
    @org.junit.jupiter.api.Order(22)
    @DisplayName("Should update order status successfully")
    void shouldUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(orderRepository.save(any(com.kauanferreira.smartorder.entity.Order.class)))
                .thenReturn(order1);

        com.kauanferreira.smartorder.entity.Order result =
                orderService.updateStatus(1L, OrderStatus.CONFIRMED);

        assertThat(result).isNotNull();
        assertThat(order1.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(order1);
    }

    @Test
    @org.junit.jupiter.api.Order(23)
    @DisplayName("Should throw exception when updating status of non-existent order")
    void shouldThrowExceptionWhenUpdatingStatusOfNonExistentOrder() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.updateStatus(99L, OrderStatus.CONFIRMED))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 99 not found");

        verify(orderRepository).findById(99L);
        verify(orderRepository, never()).save(any());
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(24)
    @DisplayName("Should update order successfully")
    void shouldUpdateOrder() {
        com.kauanferreira.smartorder.entity.Order updatedData =
                new com.kauanferreira.smartorder.entity.Order();
        updatedData.setStatus(OrderStatus.SHIPPED);
        updatedData.setTotalAmount(new BigDecimal("250.00"));
        updatedData.setAddress(address2);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(addressService.findById(2L)).thenReturn(address2);
        when(orderRepository.save(any(com.kauanferreira.smartorder.entity.Order.class)))
                .thenReturn(order1);

        com.kauanferreira.smartorder.entity.Order result = orderService.update(1L, updatedData);

        assertThat(result).isNotNull();
        assertThat(order1.getStatus()).isEqualTo(OrderStatus.SHIPPED);
        assertThat(order1.getTotalAmount()).isEqualByComparingTo(new BigDecimal("250.00"));
        assertThat(order1.getAddress().getId()).isEqualTo(2L);
        verify(orderRepository).findById(1L);
        verify(addressService).findById(2L);
        verify(orderRepository).save(order1);
    }

    @Test
    @org.junit.jupiter.api.Order(25)
    @DisplayName("Should throw exception when updating non-existent order")
    void shouldThrowExceptionWhenUpdatingNonExistentOrder() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.update(99L, order1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 99 not found");

        verify(orderRepository).findById(99L);
        verify(orderRepository, never()).save(any());
    }

    @Test
    @org.junit.jupiter.api.Order(26)
    @DisplayName("Should throw exception when updating order with non-existent address")
    void shouldThrowExceptionWhenUpdatingOrderWithNonExistentAddress() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));
        when(addressService.findById(1L)).thenThrow(
                new EntityNotFoundException("Address not found. Id: 1"));

        assertThatThrownBy(() -> orderService.update(1L, order1))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Address not found");

        verify(orderRepository).findById(1L);
        verify(addressService).findById(1L);
        verify(orderRepository, never()).save(any());
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(27)
    @DisplayName("Should delete order successfully")
    void shouldDeleteOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order1));

        orderService.delete(1L);

        verify(orderRepository).findById(1L);
        verify(orderRepository).delete(order1);
    }

    @Test
    @org.junit.jupiter.api.Order(28)
    @DisplayName("Should throw exception when deleting non-existent order")
    void shouldThrowExceptionWhenDeletingNonExistentOrder() {
        when(orderRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderService.delete(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Order with id 99 not found");

        verify(orderRepository).findById(99L);
        verify(orderRepository, never()).delete(any());
    }
}
