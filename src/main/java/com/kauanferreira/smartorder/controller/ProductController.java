package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.ProductMapper;
import com.kauanferreira.smartorder.dto.request.ProductRequest;
import com.kauanferreira.smartorder.dto.response.ProductResponse;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
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

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link Product} resources.
 *
 * <p>Provides endpoints for CRUD operations, search, filtering,
 * and activation/deactivation of products.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ProductService
 * @see ProductMapper
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    /**
     * Creates a new product.
     *
     * @param request the product data
     * @return HTTP 201 with the created product and location header
     */
    @Operation(summary = "Create a new product", description = "Creates a new product linked to an existing category.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Product created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Product name already exists")
    })
    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product entity = ProductMapper.toEntity(request);
        Product created = productService.create(entity);
        ProductResponse response = ProductMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves a product by its id.
     *
     * @param id the product id
     * @return HTTP 200 with the product data
     */
    @Operation(summary = "Find product by ID", description = "Retrieves a single product by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> findById(@PathVariable Long id) {
        Product entity = productService.findById(id);
        return ResponseEntity.ok(ProductMapper.toResponse(entity));
    }

    /**
     * Retrieves all products.
     *
     * @return HTTP 200 with the list of products
     */
    @Operation(summary = "List all products", description = "Retrieves all products without pagination.")
    @ApiResponse(responseCode = "200", description = "List of products retrieved successfully")
    @GetMapping
    public ResponseEntity<List<ProductResponse>> findAll() {
        List<ProductResponse> response = productService.findAll()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all products with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of products
     */
    @Operation(summary = "List products with pagination", description = "Retrieves products with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of products retrieved successfully")
    @GetMapping("/paged")
    public ResponseEntity<Page<ProductResponse>> findAllPaged(Pageable pageable){
        Page<ProductResponse> responses = productService.findAll(pageable)
                .map(ProductMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all products ordered by name ascending.
     *
     * @return HTTP 200 with the ordered list of products
     */
    @Operation(summary = "List products ordered by name", description = "Retrieves all products sorted alphabetically by name.")
    @ApiResponse(responseCode = "200", description = "Ordered list retrieved successfully")
    @GetMapping("/ordered/name")
    public ResponseEntity<List<ProductResponse>> findAllOrderByNameAsc() {
        List<ProductResponse> responses = productService.findAllOrderByNameAsc()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all products ordered by price ascending.
     *
     * @return HTTP 200 with the ordered list of products
     */
    @Operation(summary = "List products ordered by price", description = "Retrieves all products sorted by price ascending.")
    @ApiResponse(responseCode = "200", description = "Ordered list retrieved successfully")
    @GetMapping("/ordered/price")
    public ResponseEntity<List<ProductResponse>> findAllOrderByPriceAsc() {
        List<ProductResponse> responses = productService.findAllOrderByPriceAsc()
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Searches products by name (case-insensitive, partial match).
     *
     * @param name the search term
     * @return HTTP 200 with the list of matching products
     */
    @Operation(summary = "Search products by name", description = "Finds products whose name contains the search term (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<List<ProductResponse>> searchByName(@RequestParam String name) {
        List<ProductResponse> responses = productService.searchByName(name)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all products in a specific category.
     *
     * @param categoryId the category id
     * @return HTTP 200 with the list of products
     */
    @Operation(summary = "Find products by category", description = "Retrieves all products belonging to a specific category.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> findAllByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponse> responses = productService.findByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all products filtered by active status.
     *
     * @param active the active status
     * @return HTTP 200 with the list of products
     */
    @Operation(summary = "Find products by active status", description = "Retrieves products filtered by their active/inactive status.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/active/{active}")
    public ResponseEntity<List<ProductResponse>> findAllByActive(@PathVariable Boolean active) {
        List<ProductResponse> responses = productService.findByActive(active)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all active products in a specific category.
     *
     * @param categoryId the category id
     * @return HTTP 200 with the list of active products
     */
    @Operation(summary = "Find active products by category", description = "Retrieves only active products belonging to a specific category.")
    @ApiResponse(responseCode = "200", description = "Active products retrieved successfully")
    @GetMapping("/category/{categoryId}/active")
    public ResponseEntity<List<ProductResponse>> findAllActiveByCategoryId(@PathVariable Long categoryId) {
        List<ProductResponse> responses = productService.findActiveByCategoryId(categoryId)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all products within a price range.
     *
     * @param min the minimum price
     * @param max the maximum price
     * @return HTTP 200 with the list of products
     */
    @Operation(summary = "Find products by price range", description = "Retrieves products within a minimum and maximum price range.")
    @ApiResponse(responseCode = "200", description = "Products retrieved successfully")
    @GetMapping("/price")
    public ResponseEntity<List<ProductResponse>> findByPriceRange(@RequestParam BigDecimal min,
                                                                   BigDecimal max){
        List<ProductResponse> responses = productService.findByPriceRange(min, max)
                .stream()
                .map(ProductMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Activates a product.
     *
     * @param id the product id
     * @return HTTP 200 with the activated product
     */
    @Operation(summary = "Activate a product", description = "Sets a product's active status to true.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product activated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/activate")
    public ResponseEntity<ProductResponse> activate(@PathVariable Long id) {
        Product activate = productService.activate(id);
        return ResponseEntity.ok(ProductMapper.toResponse(activate));
    }

    /**
     * Deactivates a product.
     *
     * @param id the product id
     * @return HTTP 200 with the deactivated product
     */
    @Operation(summary = "Deactivate a product", description = "Sets a product's active status to false.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product deactivated successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<ProductResponse> deactivate(@PathVariable Long id) {
        Product deactivate = productService.deactivate(id);
        return ResponseEntity.ok(ProductMapper.toResponse(deactivate));
    }

    /**
     * Updates an existing product.
     *
     * @param id      the id of the product to update
     * @param request the updated product data
     * @return HTTP 200 with the updated product
     */
    @Operation(summary = "Update a product", description = "Updates an existing product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Product or category not found"),
            @ApiResponse(responseCode = "409", description = "Product name already exists")
    }) @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody ProductRequest request) {
        Product entity = ProductMapper.toEntity(request);
        Product updated = productService.update(id, entity);
        return ResponseEntity.ok(ProductMapper.toResponse(updated));
    }

    /**
     * Deletes a product by its id.
     *
     * @param id the id of the product to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete a product", description = "Deletes a product by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Product deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductResponse> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
