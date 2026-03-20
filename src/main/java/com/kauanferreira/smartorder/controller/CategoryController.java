package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.CategoryMapper;
import com.kauanferreira.smartorder.dto.request.CategoryRequest;
import com.kauanferreira.smartorder.dto.response.CategoryResponse;
import com.kauanferreira.smartorder.entity.Category;
import com.kauanferreira.smartorder.services.interfaces.CategoryService;
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
 * REST controller for managing {@link Category} resources.
 *
 * <p>Provides endpoints for CRUD operations and search methods
 * on categories in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see CategoryService
 * @see CategoryMapper
 */
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Categories", description = "Endpoints for managing product categories")
public class CategoryController {

    private final CategoryService categoryService;

    /**
     * Creates a new category.
     *
     * @param request the category data
     * @return HTTP 201 with the created category and location header
     */
    @Operation(summary = "Create a new category", description = "Creates a new product category and returns it with a Location header.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Category created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        Category entity = CategoryMapper.toEntity(request);
        Category created = categoryService.create(entity);
        CategoryResponse response = CategoryMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves a category by its id.
     *
     * @param id the category id
     * @return HTTP 200 with the category data
     */
    @Operation(summary = "Find category by ID", description = "Retrieves a single category by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category found"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(CategoryMapper.toResponse(category));
    }

    /**
     * Retrieves all categories.
     *
     * @return HTTP 200 with the list of categories
     */
    @Operation(summary = "List all categories", description = "Retrieves all categories without pagination.")
    @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findAll() {
        List<CategoryResponse> responses = categoryService.findAll()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all categories with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of categories
     */
    @Operation(summary = "List categories with pagination", description = "Retrieves categories with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of categories retrieved successfully")
    @GetMapping("/paged")
    public ResponseEntity<Page<CategoryResponse>> findAllPaged(Pageable pageable) {
        Page<CategoryResponse> responses = categoryService.findAll(pageable)
                .map(CategoryMapper::toResponse);
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all categories ordered by name ascending.
     *
     * @return HTTP 200 with the ordered list of categories
     */
    @Operation(summary = "List categories ordered by name", description = "Retrieves all categories sorted alphabetically by name.")
    @ApiResponse(responseCode = "200", description = "Ordered list retrieved successfully")
    @GetMapping("/ordered")
    public ResponseEntity<List<CategoryResponse>> findAllOrderByNameAsc() {
        List<CategoryResponse> responses = categoryService.findAllOrderByNameAsc()
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Searches categories by name (case-insensitive, partial match).
     *
     * @param name the search term
     * @return HTTP 200 with the list of matching categories
     */
    @Operation(summary = "Search categories by name", description = "Finds categories whose name contains the search term (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<List<CategoryResponse>> searchByName(@RequestParam String name) {
        List<CategoryResponse> responses = categoryService.searchByName(name)
                .stream()
                .map(CategoryMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }


    /**
     * Updates an existing category.
     *
     * @param id      the id of the category to update
     * @param request the updated category data
     * @return HTTP 200 with the updated category
     */
    @Operation(summary = "Update a category", description = "Updates an existing category by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Category not found"),
            @ApiResponse(responseCode = "409", description = "Category name already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @Valid @RequestBody CategoryRequest request) {

        Category entity = CategoryMapper.toEntity(request);
        Category updated = categoryService.update(id, entity);
        return ResponseEntity.ok(CategoryMapper.toResponse(updated));
    }

    /**
     * Deletes a category by its id.
     *
     * @param id the id of the category to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete a category", description = "Deletes a category by its ID. Fails if products are linked to it.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
