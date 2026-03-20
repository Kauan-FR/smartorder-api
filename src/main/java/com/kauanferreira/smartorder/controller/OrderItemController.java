package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.OrderItemMapper;
import com.kauanferreira.smartorder.dto.request.OrderItemRequest;
import com.kauanferreira.smartorder.dto.response.OrderItemResponse;
import com.kauanferreira.smartorder.entity.OrderItem;
import com.kauanferreira.smartorder.services.interfaces.OrderItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Order Items", description = "Endpoints for managing order items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    /**
     * Creates a new order item.
     *
     * @param request the order item data
     * @return HTTP 201 with the created order item and location header
     */
    @Operation(summary = "Create a new order item", description = "Creates a new item linked to an existing order and product.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order item created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order or product not found")
    })
    @PostMapping
    public ResponseEntity<OrderItemResponse> create(@Valid @RequestBody OrderItemRequest request) {
        OrderItem entity = OrderItemMapper.toEntity(request);
        OrderItem created = orderItemService.create(entity);
        OrderItem fullItem = orderItemService.findById(created.getId());
        OrderItemResponse response = OrderItemMapper.toResponse(fullItem);

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
    @Operation(summary = "Find order item by ID", description = "Retrieves a single order item with product and order details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order item found"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
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
    @Operation(summary = "List all order items", description = "Retrieves all order items without pagination.")
    @ApiResponse(responseCode = "200", description = "List of order items retrieved successfully")
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
    @Operation(summary = "List order items with pagination", description = "Retrieves order items with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of order items retrieved successfully")
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
    @Operation(summary = "Find items by order", description = "Retrieves all items belonging to a specific order.")
    @ApiResponse(responseCode = "200", description = "Order items retrieved successfully")
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
    @Operation(summary = "Find items by product", description = "Retrieves all order items referencing a specific product.")
    @ApiResponse(responseCode = "200", description = "Order items retrieved successfully")
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
    @Operation(summary = "Count items by order", description = "Returns the total number of items in a specific order.")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
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
    @Operation(summary = "Update an order item", description = "Updates an existing order item. Does not change the order association.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order item or product not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderItemResponse> update(@PathVariable Long id,
                                                    @Valid @RequestBody OrderItemRequest request) {
        OrderItem entity = OrderItemMapper.toEntity(request);
        orderItemService.update(id, entity);
        OrderItem fullItem = orderItemService.findById(id);
        return ResponseEntity.ok(OrderItemMapper.toResponse(fullItem));
    }

    /**
     * Deletes an order item by its id.
     *
     * @param id the id of the order item to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete an order item", description = "Deletes an order item by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order item deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
