package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.AddressMapper;
import com.kauanferreira.smartorder.dto.request.AddressRequest;
import com.kauanferreira.smartorder.dto.response.AddressResponse;
import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * REST controller for managing {@link Address} resources.
 *
 * <p>Provides endpoints for CRUD operations and search methods
 * on addresses in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see AddressService
 * @see AddressMapper
 */
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Endpoints for managing user addresses")
public class AddressController {

    private final AddressService addressService;

    /**
     * Creates a new address.
     *
     * @param request the address data
     * @return HTTP 201 with the created address and location header
     */
    @Operation(summary = "Create a new address", description = "Creates a new address linked to an existing user.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Address created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping
    public ResponseEntity<AddressResponse> create(@Valid @RequestBody AddressRequest request) {
        Address entity = AddressMapper.toEntity(request);
        Address created = addressService.create(entity);
        AddressResponse response = AddressMapper.toResponse(created);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(response);
    }

    /**
     * Retrieves an address by its id.
     *
     * @param id the address id
     * @return HTTP 200 with the address data
     */
    @Operation(summary = "Find address by ID", description = "Retrieves a single address by its unique identifier.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> findById(@PathVariable Long id) {
        Address address = addressService.findById(id);
        return ResponseEntity.ok().body(AddressMapper.toResponse(address));
    }

    /**
     * Retrieves all addresses.
     *
     * @return HTTP 200 with the list of addresses
     */
    @Operation(summary = "List all addresses", description = "Retrieves all addresses without pagination.")
    @ApiResponse(responseCode = "200", description = "List of addresses retrieved successfully")
    @GetMapping
    public ResponseEntity<List<AddressResponse>> findAll() {
        List<AddressResponse> responses = addressService.findAll()
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Retrieves all addresses with pagination support.
     *
     * @param pageable pagination parameters (page, size, sort)
     * @return HTTP 200 with a page of addresses
     */
    @Operation(summary = "List addresses with pagination", description = "Retrieves addresses with pagination support (page, size, sort).")
    @ApiResponse(responseCode = "200", description = "Page of addresses retrieved successfully")
    @GetMapping("/paged")
    public ResponseEntity<Page<AddressResponse>> findAllPaged(Pageable pageable) {
        Page<AddressResponse> responses = addressService.findAll(pageable)
                .map(AddressMapper::toResponse);
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Retrieves all addresses belonging to a specific user.
     *
     * @param userId the user id
     * @return HTTP 200 with the list of addresses
     */
    @Operation(summary = "Find addresses by user", description = "Retrieves all addresses belonging to a specific user.")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> findByUserId(@PathVariable Long userId) {
        List<AddressResponse> responses = addressService.findByUserId(userId)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Retrieves all addresses in a specific city.
     *
     * @param city the city name
     * @return HTTP 200 with the list of addresses
     */
    @Operation(summary = "Find addresses by city", description = "Retrieves all addresses in a specific city (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping("/city/{city}")
    public ResponseEntity<List<AddressResponse>> findByCity(@PathVariable String city) {
        List<AddressResponse> responses = addressService.findByCity(city)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Retrieves all addresses in a specific state.
     *
     * @param state the state name
     * @return HTTP 200 with the list of addresses
     */
    @Operation(summary = "Find addresses by state", description = "Retrieves all addresses in a specific state (case-insensitive).")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping("/state/{state}")
    public ResponseEntity<List<AddressResponse>> findByStates(@PathVariable String state) {
        List<AddressResponse> responses = addressService.findByState(state)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Retrieves all addresses with a specific zip code.
     *
     * @param zipCode the zip code
     * @return HTTP 200 with the list of addresses
     */
    @Operation(summary = "Find addresses by zip code", description = "Retrieves all addresses with a specific zip code.")
    @ApiResponse(responseCode = "200", description = "Addresses retrieved successfully")
    @GetMapping("/zipcode/{zipCode}")
    public ResponseEntity<List<AddressResponse>> findByZipCode(@PathVariable String zipCode) {
        List<AddressResponse> responses = addressService.findByZipCode(zipCode)
                .stream()
                .map(AddressMapper::toResponse)
                .toList();
        return ResponseEntity.ok().body(responses);
    }

    /**
     * Counts the total number of addresses for a specific user.
     *
     * @param userId the user id
     * @return HTTP 200 with the address count
     */
    @Operation(summary = "Count addresses by user", description = "Returns the total number of addresses for a specific user.")
    @ApiResponse(responseCode = "200", description = "Count retrieved successfully")
    @GetMapping("/user/{userId}/count")
    public ResponseEntity<Long> countByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(addressService.countByUserId(userId));
    }

    /**
     * Updates an existing address.
     *
     * @param id      the id of the address to update
     * @param request the updated address data
     * @return HTTP 200 with the updated address
     */
    @Operation(summary = "Update an address", description = "Updates an existing address by its ID. Does not change the user association.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Address updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> update(@PathVariable Long id,
                                                  @Valid @RequestBody AddressRequest request) {
        Address entity = AddressMapper.toEntity(request);
        Address updated = addressService.update(id, entity);
        return ResponseEntity.ok().body(AddressMapper.toResponse(updated));
    }

    /**
     * Deletes an address by its id.
     *
     * @param id the id of the address to delete
     * @return HTTP 204 with no content
     */
    @Operation(summary = "Delete an address", description = "Deletes an address by its ID.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Address deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<AddressResponse> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
