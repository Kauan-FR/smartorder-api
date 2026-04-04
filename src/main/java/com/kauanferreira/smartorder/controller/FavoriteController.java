package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.FavoriteMapper;
import com.kauanferreira.smartorder.dto.response.FavoriteResponse;
import com.kauanferreira.smartorder.entity.Favorite;
import com.kauanferreira.smartorder.services.interfaces.FavoriteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing the authenticated user's favorites/wishlist.
 *
 * <p>All endpoints operate on the favorites of the currently authenticated user,
 * identified by the JWT token. Users can only access and modify their own favorites.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see FavoriteService
 * @see FavoriteMapper
 */
@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
@Tag(name = "Favorites", description = "Endpoints for managing the favorites/wishlist")
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
     * Adds a product to the authenticated user's favorites.
     * If the product is already favorited, returns the existing favorite.
     *
     * @param authentication the authenticated user's security context
     * @param productId      the ID of the product to favorite
     * @return HTTP 201 with the created favorite and location header
     */
    @Operation(summary = "Add to favorites", description = "Adds a product to the favorites. If already favorited, returns the existing entry.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product added to favorites successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PostMapping("/{productId}")
    public ResponseEntity<FavoriteResponse> addFavorite(Authentication authentication,
                                                        @PathVariable Long productId) {
        Favorite created = favoriteService.addFavorite(authentication.getName(), productId);
        FavoriteResponse response = FavoriteMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/api/favorites/{productId}")
                .buildAndExpand(productId)
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves all favorited products for the authenticated user.
     *
     * @param authentication the authenticated user's security context
     * @return HTTP 200 with the list of favorited products
     */
    @Operation(summary = "Get favorites", description = "Retrieves all favorited products for the authenticated user.")
    @ApiResponse(responseCode = "200", description = "Favorites retrieved successfully")
    @GetMapping
    public ResponseEntity<List<FavoriteResponse>> getFavorites(Authentication authentication) {
        List<FavoriteResponse> responses = favoriteService.getFavorites(authentication.getName())
                .stream()
                .map(FavoriteMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Checks if a product is favorited by the authenticated user.
     *
     * @param authentication the authenticated user's security context
     * @param productId      the ID of the product to check
     * @return HTTP 200 with true if favorited, false otherwise
     */
    @Operation(summary = "Check if favorited", description = "Checks if a product is in the authenticated user's favorites.")
    @ApiResponse(responseCode = "200", description = "Favorite status retrieved successfully")
    @GetMapping("/{productId}/check")
    public ResponseEntity<Boolean> isFavorited(Authentication authentication,
                                               @PathVariable Long productId) {
        return ResponseEntity.ok(favoriteService.isFavorited(authentication.getName(), productId));
    }

    /**
     * Removes a product from the authenticated user's favorites.
     *
     * @param authentication the authenticated user's security context
     * @param productId      the ID of the product to unfavorite
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Remove from favorites", description = "Removes a product from the authenticated user's favorites.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product removed from favorites successfully"),
            @ApiResponse(responseCode = "404", description = "Product not in favorites")
    })
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFavorite(Authentication authentication,
                                               @PathVariable Long productId) {
        favoriteService.removeFavorite(authentication.getName(), productId);
        return ResponseEntity.noContent().build();
    }
}
