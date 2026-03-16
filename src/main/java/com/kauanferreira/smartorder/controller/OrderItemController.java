package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.OrderItemMapper;
import com.kauanferreira.smartorder.dto.request.OrderItemRequest;
import com.kauanferreira.smartorder.dto.response.OrderItemResponse;
import com.kauanferreira.smartorder.entity.OrderItem;
import com.kauanferreira.smartorder.services.interfaces.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link OrderItem} resources.
 *
 * <p>Provides endpoints for CRUD operations and search methods
 * on order items in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderItemService
 * @see OrderItemMapper
 */
@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Creates a new order item.
     *
     * @param request the order item data
     * @return HTTP 201 with the created order item and location header
     */
    @PostMapping
    public ResponseEntity<OrderItemResponse> create(@Valid @RequestBody OrderItemRequest request) {
        OrderItem entity = OrderItemMapper.toEntity(request);
        OrderItem created = orderItemService.create(entity);
        OrderItemResponse response = OrderItemMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves an order item by its id.
     *
     * @param id the order item id
     * @return HTTP 200 with the order item data
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderItemResponse> findById(@PathVariable Long id) {
        OrderItem orderItem = orderItemService.findById(id);
        return ResponseEntity.ok(OrderItemMapper.toResponse(orderItem));
    }

    /**
     * Retrieves all order items.
     *
     * @return HTTP 200 with the list of order items
     */
    @GetMapping
    public ResponseEntity<List<OrderItemResponse>> findAll() {
        List<OrderItemResponse> responses = orderItemService.findAll()
                .stream()
                .map(OrderItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all order items with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of order items
     */
    @GetMapping("/paged")
    public ResponseEntity<Page<OrderItemResponse>> findAll(Pageable pageable) {
        Page<OrderItemResponse> responses = orderItemService.findAll(pageable)
                .map(OrderItemMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all items belonging to a specific order.
     *
     * @param orderId the order id
     * @return HTTP 200 with the list of order items
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<OrderItemResponse>> findByOrderId(@PathVariable Long orderId) {
        List<OrderItemResponse> responses = orderItemService.findByOrderId(orderId)
                .stream()
                .map(OrderItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all order items referencing a specific product.
     *
     * @param productId the product id
     * @return HTTP 200 with the list of order items
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<OrderItemResponse>> findByProductId(@PathVariable Long productId) {
        List<OrderItemResponse> responses = orderItemService.findByProductId(productId)
                .stream()
                .map(OrderItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Counts the total number of items in a specific order.
     *
     * @param orderId the order id
     * @return HTTP 200 with the item count
     */
    @GetMapping("/order/{orderId}/count")
    public ResponseEntity<Long> countByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderItemService.countByOrderId(orderId));
    }

    /**
     * Updates an existing order item.
     *
     * @param id      the id of the order item to update
     * @param request the updated order item data
     * @return HTTP 200 with the updated order item
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody OrderItemRequest request) {
        OrderItem entity = OrderItemMapper.toEntity(request);
        OrderItem updated = orderItemService.update(id, entity);
        return ResponseEntity.ok(OrderItemMapper.toResponse(updated));
    }

    /**
     * Deletes an order item by its id.
     *
     * @param id the id of the order item to delete
     * @return HTTP 204 with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
