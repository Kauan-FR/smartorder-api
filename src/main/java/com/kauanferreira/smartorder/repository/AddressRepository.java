package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Address;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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

    @Query("SELECT a FROM Address a JOIN FETCH a.user")
    List<Address> findAll();

    @Query("SELECT a FROM Address a JOIN FETCH a.user WHERE a.id = :id")
    Optional<Address> findById(@Param("id") Long id);

    @Query(value = "SELECT a FROM Address a JOIN FETCH a.user",
            countQuery = "SELECT COUNT(a) FROM Address a")
    Page<Address> findAll(Pageable pageable);
    /**
     * Finds all addresses belonging to a specific user.
     *
     * @param userId the ID of the user
     * @return a list of addresses for the given user
     */
    @Query("SELECT a FROM Address a JOIN FETCH a.user WHERE a.user.id = :userId")
    List<Address> findByUserId(@Param("userId") Long userId);

    /**
     * Finds addresses by city, ignoring case.
     *
     * @param city the city name to search for
     * @return a list of addresses in the given city
     */
    @Query("SELECT a FROM Address a JOIN FETCH a.user WHERE LOWER(a.city) = LOWER(:city)")
    List<Address> findByCityIgnoreCase(@Param("city") String city);

    /**
     * Finds addresses by state, ignoring case.
     *
     * @param state the state name to search for
     * @return a list of addresses in the given state
     */
    @Query("SELECT a FROM Address a JOIN FETCH a.user WHERE LOWER(a.state) = LOWER(:state)")
    List<Address> findByStateIgnoreCase(@Param("state") String state);

    /**
     * Finds addresses by zip code.
     *
     * @param zipCode the zip code to search for
     * @return a list of addresses with the given zip code
     */
    @Query("SELECT a FROM Address a JOIN FETCH a.user WHERE a.zipCode = :zipCode")
    List<Address> findByZipCode(@Param("zipCode") String zipCode);

    /**
     * Counts how many addresses a user has.
     *
     * @param userId the ID of the user
     * @return the number of addresses for the given user
     */
    long countByUserId(Long userId);
}