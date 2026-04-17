package com.kauanferreira.smartorder.service.impl;

import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.entity.OrderItem;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.OrderItemRepository;
import com.kauanferreira.smartorder.services.impl.OrderItemServiceImpl;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import org.junit.jupiter.api.*;
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
 * Unit tests for {@link OrderItemServiceImpl}.
 *
 * <p>Uses Mockito to mock {@link OrderItemRepository}, {@link OrderService},
 * and {@link ProductService}, testing all CRUD operations and query methods
 * in isolation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderItemServiceImpl
 * @see OrderItemRepository
 */
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemServiceImplTest {

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OrderItemServiceImpl orderItemService;

    private Product product1;
    private Product product2;
    private Order order;
    private OrderItem orderItem1;
    private OrderItem orderItem2;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setId(1L);
        user.setName("John Doe");
        user.setEmail("john@email.com");
        user.setPassword("password123");
        user.setRole(Role.CUSTOMER);

        Address address = new Address();
        address.setId(1L);
        address.setStreet("Rua A");
        address.setNumber("100");
        address.setCity("Aracaju");
        address.setState("Sergipe");
        address.setZipCode("49000-000");
        address.setCountry("Brasil");
        address.setUser(user);

        Category category = new Category();
        category.setId(1L);
        category.setName("Electronics");

        product1 = new Product();
        product1.setId(1L);
        product1.setName("Keyboard");
        product1.setPrice(new BigDecimal("80.00"));
        product1.setStockQuantity(50);
        product1.setActive(true);
        product1.setCategory(category);

        product2 = new Product();
        product2.setId(2L);
        product2.setName("Mouse");
        product2.setPrice(new BigDecimal("45.00"));
        product2.setStockQuantity(100);
        product2.setActive(true);
        product2.setCategory(category);

        order = new Order();
        order.setId(1L);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(new BigDecimal("205.00"));
        order.setUser(user);
        order.setAddress(address);

        orderItem1 = new OrderItem();
        orderItem1.setId(1L);
        orderItem1.setQuantity(2);
        orderItem1.setPrice(new BigDecimal("80.00"));
        orderItem1.setSubtotal(new BigDecimal("160.00"));
        orderItem1.setOrder(order);
        orderItem1.setProduct(product1);

        orderItem2 = new OrderItem();
        orderItem2.setId(2L);
        orderItem2.setQuantity(1);
        orderItem2.setPrice(new BigDecimal("45.00"));
        orderItem2.setSubtotal(new BigDecimal("45.00"));
        orderItem2.setOrder(order);
        orderItem2.setProduct(product2);
    }

    // ========================
    // CREATE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(1)
    @DisplayName("Should create order item successfully")
    void shouldCreateOrderItem() {
        when(orderService.findById(1L)).thenReturn(order);
        when(productService.findById(1L)).thenReturn(product1);
        when(orderItemRepository.save(orderItem1)).thenReturn(orderItem1);

        OrderItem result = orderItemService.create(orderItem1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(2);
        assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("80.00"));
        assertThat(result.getSubtotal()).isEqualByComparingTo(new BigDecimal("160.00"));
        assertThat(result.getOrder().getId()).isEqualTo(1L);
        assertThat(result.getProduct().getId()).isEqualTo(1L);
        verify(orderService).findById(1L);
        verify(productService).findById(1L);
        verify(orderItemRepository).save(orderItem1);
    }

    @Test
    @org.junit.jupiter.api.Order(2)
    @DisplayName("Should throw exception when creating order item with non-existent order")
    void shouldThrowExceptionWhenCreatingWithNonExistentOrder() {
        when(orderService.findById(1L)).thenThrow(
                new ResourceNotFoundException("Order not found. Id: 1"));

        assertThatThrownBy(() -> orderItemService.create(orderItem1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order not found");

        verify(orderService).findById(1L);
        verify(productService, never()).findById(any());
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    @org.junit.jupiter.api.Order(3)
    @DisplayName("Should throw exception when creating order item with non-existent product")
    void shouldThrowExceptionWhenCreatingWithNonExistentProduct() {
        when(orderService.findById(1L)).thenReturn(order);
        when(productService.findById(1L)).thenThrow(
                new ResourceNotFoundException("Product not found. Id: 1"));

        assertThatThrownBy(() -> orderItemService.create(orderItem1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(orderService).findById(1L);
        verify(productService).findById(1L);
        verify(orderItemRepository, never()).save(any());
    }

    // ========================
    // FIND BY ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(4)
    @DisplayName("Should find order item by id successfully")
    void shouldFindOrderItemById() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem1));

        OrderItem result = orderItemService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getQuantity()).isEqualTo(2);
        verify(orderItemRepository).findById(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(5)
    @DisplayName("Should throw exception when order item not found by id")
    void shouldThrowExceptionWhenOrderItemNotFoundById() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderItemService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("OrderItem with id 99 not found");

        verify(orderItemRepository).findById(99L);
    }

    // ========================
    // FIND ALL
    // ========================

    @Test
    @org.junit.jupiter.api.Order(6)
    @DisplayName("Should return all order items")
    void shouldReturnAllOrderItems() {
        when(orderItemRepository.findAll()).thenReturn(List.of(orderItem1, orderItem2));

        List<OrderItem> result = orderItemService.findAll();

        assertThat(result).hasSize(2);
        verify(orderItemRepository).findAll();
    }

    @Test
    @org.junit.jupiter.api.Order(7)
    @DisplayName("Should return empty list when no order items exist")
    void shouldReturnEmptyListWhenNoOrderItemsExist() {
        when(orderItemRepository.findAll()).thenReturn(Collections.emptyList());

        List<OrderItem> result = orderItemService.findAll();

        assertThat(result).isEmpty();
        verify(orderItemRepository).findAll();
    }

    // ========================
    // FIND ALL PAGINATED
    // ========================

    @Test
    @org.junit.jupiter.api.Order(8)
    @DisplayName("Should return order items with pagination")
    void shouldReturnOrderItemsWithPagination() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<OrderItem> page = new PageImpl<>(List.of(orderItem1, orderItem2), pageable, 2);
        when(orderItemRepository.findAll(pageable)).thenReturn(page);

        Page<OrderItem> result = orderItemService.findAll(pageable);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getTotalElements()).isEqualTo(2);
        verify(orderItemRepository).findAll(pageable);
    }

    // ========================
    // FIND BY ORDER ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(9)
    @DisplayName("Should return order items by order id")
    void shouldReturnOrderItemsByOrderId() {
        when(orderService.findById(1L)).thenReturn(order);
        when(orderItemRepository.findByOrderId(1L)).thenReturn(List.of(orderItem1, orderItem2));

        List<OrderItem> result = orderItemService.findByOrderId(1L);

        assertThat(result).hasSize(2);
        verify(orderService).findById(1L);
        verify(orderItemRepository).findByOrderId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(10)
    @DisplayName("Should return empty list when order has no items")
    void shouldReturnEmptyListWhenOrderHasNoItems() {
        when(orderService.findById(1L)).thenReturn(order);
        when(orderItemRepository.findByOrderId(1L)).thenReturn(Collections.emptyList());

        List<OrderItem> result = orderItemService.findByOrderId(1L);

        assertThat(result).isEmpty();
        verify(orderService).findById(1L);
        verify(orderItemRepository).findByOrderId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(11)
    @DisplayName("Should throw exception when finding items by non-existent order id")
    void shouldThrowExceptionWhenFindingByNonExistentOrderId() {
        when(orderService.findById(99L)).thenThrow(
                new ResourceNotFoundException("Order not found. Id: 99"));

        assertThatThrownBy(() -> orderItemService.findByOrderId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Order not found");

        verify(orderService).findById(99L);
        verify(orderItemRepository, never()).findByOrderId(any());
    }

    // ========================
    // FIND BY PRODUCT ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(12)
    @DisplayName("Should return order items by product id")
    void shouldReturnOrderItemsByProductId() {
        when(productService.findById(1L)).thenReturn(product1);
        when(orderItemRepository.findByProductId(1L)).thenReturn(List.of(orderItem1));

        List<OrderItem> result = orderItemService.findByProductId(1L);

        assertThat(result).hasSize(1);
        verify(productService).findById(1L);
        verify(orderItemRepository).findByProductId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(13)
    @DisplayName("Should return empty list when no items found by product id")
    void shouldReturnEmptyListWhenNoItemsFoundByProductId() {
        when(productService.findById(1L)).thenReturn(product1);
        when(orderItemRepository.findByProductId(1L)).thenReturn(Collections.emptyList());

        List<OrderItem> result = orderItemService.findByProductId(1L);

        assertThat(result).isEmpty();
        verify(productService).findById(1L);
        verify(orderItemRepository).findByProductId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(14)
    @DisplayName("Should throw exception when finding items by non-existent product id")
    void shouldThrowExceptionWhenFindingByNonExistentProductId() {
        when(productService.findById(99L)).thenThrow(
                new ResourceNotFoundException("Product not found. Id: 99"));

        assertThatThrownBy(() -> orderItemService.findByProductId(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(productService).findById(99L);
        verify(orderItemRepository, never()).findByProductId(any());
    }

    // ========================
    // COUNT BY ORDER ID
    // ========================

    @Test
    @org.junit.jupiter.api.Order(15)
    @DisplayName("Should count order items by order id")
    void shouldCountOrderItemsByOrderId() {
        when(orderItemRepository.countByOrderId(1L)).thenReturn(2L);

        long result = orderItemService.countByOrderId(1L);

        assertThat(result).isEqualTo(2L);
        verify(orderItemRepository).countByOrderId(1L);
    }

    @Test
    @org.junit.jupiter.api.Order(16)
    @DisplayName("Should return zero when order has no items")
    void shouldReturnZeroWhenOrderHasNoItems() {
        when(orderItemRepository.countByOrderId(1L)).thenReturn(0L);

        long result = orderItemService.countByOrderId(1L);

        assertThat(result).isZero();
        verify(orderItemRepository).countByOrderId(1L);
    }

    // ========================
    // UPDATE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(17)
    @DisplayName("Should update order item successfully")
    void shouldUpdateOrderItem() {
        OrderItem updatedData = new OrderItem();
        updatedData.setQuantity(3);
        updatedData.setPrice(new BigDecimal("80.00"));
        updatedData.setSubtotal(new BigDecimal("240.00"));
        updatedData.setProduct(product1);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem1));
        when(productService.findById(1L)).thenReturn(product1);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem1);

        OrderItem result = orderItemService.update(1L, updatedData);

        assertThat(result).isNotNull();
        assertThat(orderItem1.getQuantity()).isEqualTo(3);
        assertThat(orderItem1.getSubtotal()).isEqualByComparingTo(new BigDecimal("240.00"));
        assertThat(orderItem1.getProduct().getId()).isEqualTo(1L);
        verify(orderItemRepository).findById(1L);
        verify(productService).findById(1L);
        verify(orderItemRepository).save(orderItem1);
    }

    @Test
    @org.junit.jupiter.api.Order(18)
    @DisplayName("Should update order item with different product")
    void shouldUpdateOrderItemWithDifferentProduct() {
        OrderItem updatedData = new OrderItem();
        updatedData.setQuantity(1);
        updatedData.setPrice(new BigDecimal("45.00"));
        updatedData.setSubtotal(new BigDecimal("45.00"));
        updatedData.setProduct(product2);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem1));
        when(productService.findById(2L)).thenReturn(product2);
        when(orderItemRepository.save(any(OrderItem.class))).thenReturn(orderItem1);

        OrderItem result = orderItemService.update(1L, updatedData);

        assertThat(result).isNotNull();
        assertThat(orderItem1.getProduct().getId()).isEqualTo(2L);
        assertThat(orderItem1.getProduct().getName()).isEqualTo("Mouse");
        verify(productService).findById(2L);
        verify(orderItemRepository).save(orderItem1);
    }

    @Test
    @org.junit.jupiter.api.Order(19)
    @DisplayName("Should throw exception when updating non-existent order item")
    void shouldThrowExceptionWhenUpdatingNonExistentOrderItem() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderItemService.update(99L, orderItem1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("OrderItem with id 99 not found");

        verify(orderItemRepository).findById(99L);
        verify(orderItemRepository, never()).save(any());
    }

    @Test
    @org.junit.jupiter.api.Order(20)
    @DisplayName("Should throw exception when updating order item with non-existent product")
    void shouldThrowExceptionWhenUpdatingWithNonExistentProduct() {
        OrderItem updatedData = new OrderItem();
        updatedData.setQuantity(1);
        updatedData.setPrice(new BigDecimal("50.00"));
        updatedData.setSubtotal(new BigDecimal("50.00"));
        updatedData.setProduct(product1);

        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem1));
        when(productService.findById(1L)).thenThrow(
                new ResourceNotFoundException("Product not found. Id: 1"));

        assertThatThrownBy(() -> orderItemService.update(1L, updatedData))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Product not found");

        verify(orderItemRepository).findById(1L);
        verify(productService).findById(1L);
        verify(orderItemRepository, never()).save(any());
    }

    // ========================
    // DELETE
    // ========================

    @Test
    @org.junit.jupiter.api.Order(21)
    @DisplayName("Should delete order item successfully")
    void shouldDeleteOrderItem() {
        when(orderItemRepository.findById(1L)).thenReturn(Optional.of(orderItem1));

        orderItemService.delete(1L);

        verify(orderItemRepository).findById(1L);
        verify(orderItemRepository).delete(orderItem1);
    }

    @Test
    @org.junit.jupiter.api.Order(22)
    @DisplayName("Should throw exception when deleting non-existent order item")
    void shouldThrowExceptionWhenDeletingNonExistentOrderItem() {
        when(orderItemRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderItemService.delete(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("OrderItem with id 99 not found");

        verify(orderItemRepository).findById(99L);
        verify(orderItemRepository, never()).delete(any());
    }

}
