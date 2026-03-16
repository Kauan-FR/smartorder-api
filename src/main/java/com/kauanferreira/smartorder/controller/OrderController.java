package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.OrderMapper;
import com.kauanferreira.smartorder.dto.request.OrderRequest;
import com.kauanferreira.smartorder.dto.response.OrderResponse;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link Order} resources.
 *
 * <p>Provides endpoints for CRUD operations, search, filtering,
 * and status management of orders.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderService
 * @see OrderMapper
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order.
     *
     * @param request the order data
     * @return HTTP 201 with the created order and location header
     */
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        Order entity = OrderMapper.toEntity(request);
        Order created = orderService.create(entity);
        OrderResponse response = OrderMapper.toResponser(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves an order by its id.
     *
     * @param id the order id
     * @return HTTP 200 with the order data
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        Order order = orderService.findById(id);
        return ResponseEntity.ok(OrderMapper.toResponser(order));
    }

    /**
     * Retrieves all orders.
     *
     * @return HTTP 200 with the list of orders
     */
    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        List<OrderResponse> responses = orderService.findAll()
                .stream()
                .map(OrderMapper::toResponser)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all orders with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of orders
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<OrderResponse>> findAll(Pageable pageable) {
        Page<OrderResponse> responses = orderService.findAll(pageable)
                .map(OrderMapper::toResponser);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all orders belonging to a specific user.
     *
     * @param userId the user id
     * @return HTTP 200 with the list of orders
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> findByUserId(@PathVariable Long userId) {
        List<OrderResponse> responses = orderService.findByUserId(userId)
                .stream()
                .map(OrderMapper::toResponser)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status the order status
     * @return HTTP 200 with the list of orders
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrderResponse>> findByStatus(@PathVariable OrderStatus status) {
        List<OrderResponse> responses = orderService.findByStatus(status)
                .stream()
                .map(OrderMapper::toResponser)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all orders belonging to a specific user with a specific status.
     *
     * @param userId the user id
     * @param status the order status
     * @return HTTP 200 with the list of orders
     */
    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<OrderResponse>> findByUserIdAndStatus(@PathVariable Long userId,
                                                                     @PathVariable OrderStatus status) {
        List<OrderResponse> responses = orderService.findByUserIdAndStatus(userId, status)
                .stream()
                .map(OrderMapper::toResponser)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all orders associated with a specific address.
     *
     * @param addressId the address id
     * @return HTTP 200 with the list of orders
     */
    @GetMapping("/address/{addressId}")
    public ResponseEntity<List<OrderResponse>> findByAddressId(@PathVariable Long addressId) {
        List<OrderResponse> responses = orderService.findByAddressId(addressId)
                .stream()
                .map(OrderMapper::toResponser)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Counts the total number of orders for a specific user.
     *
     * @param userId the user id
     * @return HTTP 200 with the order count
     */
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.countByUserId(userId));
    }

    /**
     * Counts the total number of orders with a specific status.
     *
     * @param status the order status
     * @return HTTP 200 with the order count
     */
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.countByStatus(status));
    }

    /**
     * Updates the status of an existing order.
     *
     * @param id     the order id
     * @param status the new order status
     * @return HTTP 200 with the updated order
     */
    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @PathVariable OrderStatus status) {
        Order updated = orderService.updateStatus(id, status);
        return ResponseEntity.ok(OrderMapper.toResponser(updated));
    }

    /**
     * Updates an existing order.
     *
     * @param id      the id of the order to update
     * @param request the updated order data
     * @return HTTP 200 with the updated order
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody OrderRequest request) {
        Order entity = OrderMapper.toEntity(request);
        Order updated = orderService.update(id,entity);
        return ResponseEntity.ok(OrderMapper.toResponser(updated));
    }

    /**
     * Deletes an order by its id.
     *
     * @param id the id of the order to delete
     * @return HTTP 204 with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

