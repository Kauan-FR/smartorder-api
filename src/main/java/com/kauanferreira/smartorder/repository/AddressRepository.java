package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Address} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing user delivery addresses.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    /**
     * Finds all addresses belonging to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of addresses for the given user
     */
    List<Address> findByUserId(Long userId);

    /**
     * Finds addresses by city, ignoring case.
     *
     * @param city the city name to search for
     * @return a list of addresses in the given city
     */
    List<Address> findByCityIgnoreCase(String city);

    /**
     * Finds addresses by state, ignoring case.
     *
     * @param state the state name to search for
     * @return a list of addresses in the given state
     */
    List<Address> findByStateIgnoreCase(String state);

    /**
     * Finds addresses by zip code.
     *
     * @param zipCode the zip code to search for
     * @return a list of addresses with the given zip code
     */
    List<Address> findByZipCode(String zipCode);

    /**
     * Counts how many addresses a user has.
     *
     * @param userId the ID of the user
     * @return the number of addresses for the given user
     */
    long countByUserId(Long userId);
}