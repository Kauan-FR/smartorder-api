package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.OrderMapper;
import com.kauanferreira.smartorder.dto.request.OrderRequest;
import com.kauanferreira.smartorder.dto.response.OrderResponse;
import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
@Tag(name = "Orders", description = "Endpoints for managing customer orders")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order.
     *
     * @param request the order data
     * @return HTTP 201 with the created order and location header
     */
    @Operation(summary = "Create a new order", description = "Creates a new order linked to an existing user and address.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User or address not found")
    })
    @PostMapping
    public ResponseEntity<OrderResponse> create(@Valid @RequestBody OrderRequest request) {
        Order entity = OrderMapper.toEntity(request);
        Order created = orderService.create(entity);
        Order fullOrder = orderService.findById(created.getId());
        OrderResponse response = OrderMapper.toResponser(fullOrder);

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
    @Operation(summary = "Find order by ID", description = "Retrieves a single order with user, address, and items details.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order found"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
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
    @Operation(summary = "List all orders", description = "Retrieves all orders without pagination.")
    @ApiResponse(responseCode = "200", description = "List of orders retrieved successfully")
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
    @Operation(summary = "List orders with pagination", description = "Retrieves orders with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of orders retrieved successfully")
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
    @Operation(summary = "Find orders by user", description = "Retrieves all orders placed by a specific user, sorted by date descending.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
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
    @Operation(summary = "Find orders by status", description = "Retrieves all orders with a specific status (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED).")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
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
    @Operation(summary = "Find orders by user and status", description = "Retrieves all orders for a specific user filtered by status.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
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
    @Operation(summary = "Find orders by address", description = "Retrieves all orders shipped to a specific address.")
    @ApiResponse(responseCode = "200", description = "Orders retrieved successfully")
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
    @Operation(summary = "Count orders by user", description = "Returns the total number of orders placed by a specific user.")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
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
    @Operation(summary = "Count orders by status", description = "Returns the total number of orders with a specific status.")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    @GetMapping("/status/{status}/count")
    public ResponseEntity<Long> countByStatus(@PathVariable OrderStatus status) {
        return ResponseEntity.ok(orderService.countByStatus(status));
    }

    /**
     * Checks whether the authenticated user has already purchased the given product
     * in any non-cancelled order. Used by the review form to gate submission.
     */
    @Operation(
            summary = "Check if user has purchased product",
            description = "Returns true if the authenticated user has at least one non-cancelled " +
                    "order containing the given product. Used to gate review submission."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Check completed successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Boolean.class)
                    )
            ),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/has-purchased/{productId}")
    public ResponseEntity<Boolean> hasPurchased(Authentication authentication,
                                                @PathVariable Long productId) {
        return ResponseEntity.ok(
                orderService.hasUserPurchasedProduct(authentication.getName(), productId)
        );
    }

    /**
     * Updates the status of an existing order.
     *
     * @param id     the order id
     * @param status the new order status
     * @return HTTP 200 with the updated order
     */
    @Operation(summary = "Update order status", description = "Updates only the status of an existing order.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status updated successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @PatchMapping("/{id}/status/{status}")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @PathVariable OrderStatus status) {
        Order updated = orderService.updateStatus(id, status);
        Order fullOrder = orderService.findById(updated.getId());
        return ResponseEntity.ok(OrderMapper.toResponser(fullOrder));
    }

    /**
     * Updates an existing order.
     *
     * @param id      the id of the order to update
     * @param request the updated order data
     * @return HTTP 200 with the updated order
     */
    @Operation(summary = "Update an order", description = "Updates an existing order. Does not change user or order date.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Order updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Order or address not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<OrderResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody OrderRequest request) {
        Order entity = OrderMapper.toEntity(request);
        orderService.update(id,entity);
        Order fullOrder = orderService.findById(id);
        return ResponseEntity.ok(OrderMapper.toResponser(fullOrder));
    }

    /**
     * Deletes an order by its id.
     *
     * @param id the id of the order to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete an order", description = "Deletes an order and all its items by cascade.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Order deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Order not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderResponse> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

