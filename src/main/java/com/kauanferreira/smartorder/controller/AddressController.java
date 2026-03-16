package com.kauanferreira.smartorder.controller;

import com.kauanferreira.smartorder.dto.mapper.AddressMapper;
import com.kauanferreira.smartorder.dto.request.AddressRequest;
import com.kauanferreira.smartorder.dto.response.AddressResponse;
import com.kauanferreira.smartorder.entity.Address;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
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
public class AddressController {

    private final AddressService addressService;

    /**
     * Creates a new address.
     *
     * @param request the address data
     * @return HTTP 201 with the created address and location header
     */
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
    @DeleteMapping("/{id}")
    public ResponseEntity<AddressResponse> delete(@PathVariable Long id) {
        addressService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
