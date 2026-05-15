package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for {@link Address} entity operations.
 *
 * <p>Provides CRUD operations and query methods for managing
 * customer addresses in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 */
public interface AddressService {

    /**
     * Creates a new address for an existing user.
     *
     * @param address the address entity to persist
     * @return the persisted address with generated id
     */
    Address create(Address address);

    /**
     * Finds an address by its unique identifier.
     *
     * @param id the address id
     * @return the found address
     */
    Address findById(Long id);

    /**
     * Retrieves all addresses.
     *
     * @return list of all addresses, or empty list if none exist
     */
    List<Address> findAll();

    /**
     * Retrieves all addresses with pagination support.
     *
     * @param pageable pagination parameters
     * @return a page of addresses
     */
    Page<Address> findAll(Pageable pageable);

    /**
     * Retrieves all addresses belonging to a specific user.
     *
     * @param userId the user id
     * @return list of addresses for the given user, or empty list if none exist
     */
    List<Address> findByUserId(Long userId);

    /**
     * Retrieves all addresses in a specific city.
     *
     * @param city the city name (case-insensitive)
     * @return list of matching addresses, or empty list if none match
     */
    List<Address> findByCity(String city);

    /**
     * Retrieves all addresses in a specific state.
     *
     * @param state the state name (case-insensitive)
     * @return list of matching addresses, or empty list if none match
     */
    List<Address> findByState(String state);

    /**
     * Retrieves all addresses with a specific zip code.
     *
     * @param zipCode the zip code
     * @return list of matching addresses, or empty list if none match
     */
    List<Address> findByZipCode(String zipCode);

    /**
     * Counts the total number of addresses for a specific user.
     *
     * @param userId the user id
     * @return the address count for the given user
     */
    long countByUserId(Long userId);

    /**
     * Updates an existing address.
     *
     * @param id the id of the address to update
     * @param address the address data with updated fields
     * @return the updated address
     */
    Address update(Long id, Address address);

    /**
     * Retrieves all addresses belonging to the user identified by the given email.
     *
     * @param email the authenticated user's email (from JWT)
     * @return the list of addresses owned by the user
     */
    List<Address> findByAuthenticatedUser(String email);

    /**
     * Creates a new address for the user identified by the given email.
     *
     * <p>User identity is resolved from the authenticated email (JWT). The userId
     * is never accepted from the client to prevent privilege escalation.</p>
     *
     * @param email   the authenticated user's email (from JWT)
     * @param address the address entity to persist (without user association)
     * @return the persisted address with its generated id and user attached
     */
    Address createForAuthenticatedUser(String email, Address address);

    /**
     * Deletes an address belonging to the user identified by the given email.
     *
     * <p>Validates ownership before deletion — if the address exists but belongs
     * to another user, the operation is rejected as not found (to avoid leaking
     * the existence of resources).</p>
     *
     * @param email     the authenticated user's email (from JWT)
     * @param addressId the id of the address to delete
     * @throws ResourceNotFoundException if the address does not exist OR does not
     *                                    belong to the authenticated user
     */
    void deleteForAuthenticatedUser(String email, Long addressId);

    /**
     * Deletes an address by its id.
     *
     * @param id the id of the address to delete
     */
    void delete(Long id);
}
