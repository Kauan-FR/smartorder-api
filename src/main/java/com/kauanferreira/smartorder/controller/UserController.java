package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.UserMapper;
import com.kauanferreira.smartorder.dto.request.UserRequest;
import com.kauanferreira.smartorder.dto.request.UserUpdateRequest;
import com.kauanferreira.smartorder.dto.response.UserResponse;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.services.interfaces.UserService;
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
 * REST controller for managing {@link User} resources.
 *
 * <p>Provides endpoints for CRUD operations, search, and filtering
 * of users in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see UserService
 * @see UserMapper
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param request the user data
     * @return HTTP 201 with the created user and location header
     */
    @Operation(summary = "Create a new user", description = "Registers a new user in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody UserRequest request) {
        User entity = UserMapper.toEntity(request);
        User created = userService.create(entity);
        UserResponse response = UserMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves a user by its id.
     *
     * @param id the user id
     * @return HTTP 200 with the user data
     */
    @Operation(summary = "Find user by ID", description = "Retrieves a single user by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        User user = userService.findById(id);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    /**
     * Retrieves a user by email.
     *
     * @param email the user email
     * @return HTTP 200 with the user data
     */
    @Operation(summary = "Find user by email", description = "Retrieves a user by their email address.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponse> findByEmail(@PathVariable String email) {
        User user = userService.findByEmail(email);
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    /**
     * Retrieves all users.
     *
     * @return HTTP 200 with the list of users
     */
    @Operation(summary = "List all users", description = "Retrieves all users without pagination.")
    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<UserResponse> responses = userService.findAll()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all users with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of users
     */
    @Operation(summary = "List users with pagination", description = "Retrieves users with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of users retrieved successfully")
    @GetMapping("/paged")
    public ResponseEntity<Page<UserResponse>> findAllPaged(Pageable pageable) {
        Page<UserResponse> response = userService.findAll(pageable)
                .map(UserMapper::toResponse);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieves all users ordered by name ascending.
     *
     * @return HTTP 200 with the ordered list of users
     */
    @Operation(summary = "List users ordered by name", description = "Retrieves all users sorted alphabetically by name.")
    @ApiResponse(responseCode = "200", description = "Ordered list retrieved successfully")
    @GetMapping("/ordered")
    public ResponseEntity<List<UserResponse>> findAllOrderByNameAsc() {
        List<UserResponse> responses = userService.findAllOrderByNameAsc()
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Searches users by name (case-insensitive, partial match).
     *
     * @param name the search term
     * @return HTTP 200 with the list of matching users
     */
    @Operation(summary = "Search users by name", description = "Finds users whose name contains the search term (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    @GetMapping("/search")
    public ResponseEntity<List<UserResponse>> searchByName(@RequestParam String name) {
        List<UserResponse> responses = userService.searchByName(name)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Retrieves all users filtered by role.
     *
     * @param role the user role
     * @return HTTP 200 with the list of users
     */
    @Operation(summary = "Find users by role", description = "Retrieves all users with a specific role (ADMIN or CUSTOMER).")
    @ApiResponse(responseCode = "200", description = "Users retrieved successfully")
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> findByRole(@PathVariable Role role) {
        List<UserResponse> responses = userService.findByRole(role)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates the password of an existing user.
     *
     * @param id          the id of the user to update
     * @param newPassword the new password
     * @return HTTP 200 with the updated user
     */
    @Operation(summary = "Update user password", description = "Updates only the password of an existing user.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Password updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id,
                                                       @RequestParam String newPassword) {
        User updated = userService.updatePassword(id, newPassword);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    /**
     * Updates an existing user.
     *
     * @param id      the id of the user to update
     * @param request the updated user data
     * @return HTTP 200 with the updated user
     */
    @Operation(summary = "Update a user", description = "Updates an existing user by its ID. Does not update password.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserUpdateRequest request) {
        User entity = UserMapper.toEntity(request);
        User updated = userService.update(id, entity);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    /**
     * Deletes a user by its id.
     *
     * @param id the id of the user to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete a user", description = "Deletes a user by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
