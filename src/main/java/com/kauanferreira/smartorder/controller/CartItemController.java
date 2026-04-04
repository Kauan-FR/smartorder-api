package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.CartItemMapper;
import com.kauanferreira.smartorder.dto.request.CartItemRequest;
import com.kauanferreira.smartorder.dto.response.CartItemResponse;
import com.kauanferreira.smartorder.entity.CartItem;
import com.kauanferreira.smartorder.services.interfaces.CartItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing the authenticated user's shopping cart.
 *
 * <p>All endpoints operate on the cart of the currently authenticated user,
 * identified by the JWT token. Users can only access and modify their own cart.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CartItemService
 * @see CartItemMapper
 */
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Tag(name = "Cart", description = "Endpoints for managing the shopping cart")
public class CartItemController {

    private final CartItemService cartItemService;

    /**
     * Adds a product to the authenticated user's cart.
     * If the product is already in the cart, the quantity is added to the existing amount.
     *
     * @param authentication the authenticated user's security context
     * @param request        the cart item data (product ID and quantity)
     * @return HTTP 201 with the created or updated cart item and location header
     */
    @Operation(summary = "Add to cart", description = "Adds a product to the cart. If the product already exists in the cart, the quantity is added to the existing amount.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to cart successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(Authentication authentication,
                                                      @Valid @RequestBody CartItemRequest request) {
        CartItem entity = CartItemMapper.toEntity(request);
        CartItem created = cartItemService.addToCart(authentication.getName(), entity);
        CartItemResponse response = CartItemMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves all items in the authenticated user's cart.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the list of cart items
     */
    @Operation(summary = "Get cart items", description = "Retrieves all items in the authenticated user's shopping cart.")
    @ApiResponse(responseCode = "200", description = "Cart items retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(Authentication authentication) {
        List<CartItemResponse> responses = cartItemService.getCart(authentication.getName())
                .stream()
                .map(CartItemMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param authentication the authenticated user's security context
     * @param id             the cart item ID
     * @param request        the updated cart item data (new quantity)
     * @return HTTP 200 with the updated cart item
     */
    @Operation(summary = "Update cart item quantity", description = "Updates the quantity of an existing item in the authenticated user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cart item updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CartItemResponse> updateQuantity(Authentication authentication,
                                                           @PathVariable Long id,
                                                           @Valid @RequestBody CartItemRequest request) {
        CartItem updated = cartItemService.updateQuantity(authentication.getName(), id, request.quantity());
        return ResponseEntity.ok(CartItemMapper.toResponse(updated));
    }

    /**
     * Removes a single item from the authenticated user's cart.
     *
     * @param authentication the authenticated user's security context
     * @param id             the cart item ID to remove
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Remove cart item", description = "Removes a single item from the authenticated user's cart.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cart item removed successfully"),
            @ApiResponse(responseCode = "404", description = "Cart item not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeItem(Authentication authentication,
                                           @PathVariable Long id) {
        cartItemService.removeItem(authentication.getName(), id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Removes all items from the authenticated user's cart.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Clear cart", description = "Removes all items from the authenticated user's cart.")
    @ApiResponse(responseCode = "204", description = "Cart cleared successfully")
    @DeleteMapping
    public ResponseEntity<Void> clearCart(Authentication authentication) {
        cartItemService.clearCart(authentication.getName());
        return ResponseEntity.noContent().build();
    }
}