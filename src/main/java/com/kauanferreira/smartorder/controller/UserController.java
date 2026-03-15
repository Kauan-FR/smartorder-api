package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.UserMapper;
import com.kauanferreira.smartorder.dto.request.UserRequest;
import com.kauanferreira.smartorder.dto.response.UserResponse;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.services.interfaces.UserService;
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
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user.
     *
     * @param request the user data
     * @return HTTP 201 with the created user and location header
     */
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
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserResponse>> findByRole(@PathVariable Role role) {
        List<UserResponse> responses = userService.findByRole(role)
                .stream()
                .map(UserMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    /**
     * Updates an existing user.
     *
     * @param id      the id of the user to update
     * @param request the updated user data
     * @return HTTP 200 with the updated user
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody UserRequest request) {
        User entity = UserMapper.toEntity(request);
        User updated = userService.update(id, entity);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    /**
     * Updates the password of an existing user.
     *
     * @param id          the id of the user to update
     * @param newPassword the new password
     * @return HTTP 200 with the updated user
     */
    @PutMapping("/updatePassword")
    public ResponseEntity<UserResponse> updatePassword(@PathVariable Long id,
                                                       @RequestParam String newPassword) {
        User updated = userService.updatePassword(id, newPassword);
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }

    /**
     * Deletes a user by its id.
     *
     * @param id the id of the user to delete
     * @return HTTP 204 with no content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
