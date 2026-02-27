package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link User} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing users in the authentication and
 * authorization system.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Finds a user by their email address, ignoring case.
     * Used for authentication (login) and duplicate checking.
     *
     * @param email the email to search for
     * @return an Optional containing the user if found
     */
    Optional<User> findByEmailIgnoreCase(String email);

    /**
     * Checks if a user with the given email already exists.
     * Used for registration validation.
     *
     * @param email the email to check
     * @return true if the email is already registered
     */
    boolean existsByEmailIgnoreCase(String email);

    /**
     * Finds users by their role.
     *
     * @param role the role to filter by (ADMIN or CUSTOMER)
     * @return a list of users with the given role
     */
    List<User> findByRole(Role role);

    /**
     * Finds users by partial name match, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of users matching the search term
     */
    List<User> findByNameContainingIgnoreCase(String name);

    /**
     * Retrieves all users ordered by name ascending.
     *
     * @return a list of users sorted alphabetically
     */
    List<User> findAllByOrderByNameAsc();
}
