package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for managing {@link User} operations.
 *
 * <p>Defines the contract for user business logic,
 * including CRUD operations, search, and role management.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface UserService {

    /**
     * Creates a new user.
     *
     * @param user the user to create
     * @return the created user with generated ID
     */
    User create(User user);

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user
     * @return the found user
     */
    User findById(Long id);

    /**
     * Finds a user by their email address.
     *
     * @param email the email to search for
     * @return the found user
     */
    User findByEmail(String email);

    /**
     * Retrieves all users.
     *
     * @return a list of all users
     */
    List<User> findAll();

    /**
     * Retrieves all users with pagination.
     *
     * @param pageable the pagination parameters
     * @return a page of users
     */
    Page<User> findAll(Pageable pageable);

    /**
     * Retrieves all users ordered by name ascending.
     *
     * @return a list of users sorted alphabetically
     */
    List<User> findAllOrderByNameAsc();

    /**
     * Searches users by partial name match, ignoring case.
     *
     * @param name the partial name to search for
     * @return a list of matching users
     */
    List<User> searchByName(String name);

    /**
     * Finds all users with a specific role.
     *
     * @param role the role to filter by
     * @return a list of users with the given role
     */
    List<User> findByRole(Role role);

    /**
     * Updates an existing user's profile information.
     *
     * @param id the ID of the user to update
     * @param user the updated user data
     * @return the updated user
     */
    User update(Long id, User user);

    /**
     * Updates a user's password.
     *
     * @param id the ID of the user
     * @param newPassword the new password
     * @return the updated user
     */
    User updatePassword(Long id, String currentPassword, String newPassword);

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to delete
     */
    void delete(Long id);
}
