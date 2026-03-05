package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.enums.Role;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link UserService}.
 *
 * <p>Handles business logic for user management,
 * including email uniqueness validation, profile updates,
 * password management, and role-based queries.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see UserService
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * {@inheritDoc}
     *
     * @throws IllegalArgumentException if the email is already registered
     */
    @Override
    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException(
                    String.format("Email '%s' is already registered", user.getEmail()));
        }
        return userRepository.save(user);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no user is found with the given ID
     */
    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User with id " + id + " not found")
        );
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no user is found with the given email
     */
    @Override
    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new EntityNotFoundException("User with email " + email + " not found")
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findAllOrderByNameAsc() {
        return userRepository.findAllByOrderByNameAsc();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> searchByName(String name) {
        return userRepository.findByNameContainingIgnoreCase(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(Role role) {
        return userRepository.findByRole(role);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no user is found with the given ID
     * @throws IllegalArgumentException if the new email is already in use by another user
     */
    @Override
    @Transactional
    public User update(Long id, User user) {
        User existing = findById(id);

        if (!existing.getEmail().equalsIgnoreCase(user.getEmail())
                && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new IllegalArgumentException(
                    String.format("Email '%s' is already registered", user.getEmail()));
        }

        existing.setName(user.getName());
        existing.setEmail(user.getEmail());
        existing.setPhone(user.getPhone());
        existing.setRole(user.getRole());
        return userRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no user is found with the given ID
     */
    @Override
    @Transactional
    public User updatePassword(Long id, String newPassword) {
        User existing = findById(id);
        existing.setPassword(newPassword);
        return userRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws EntityNotFoundException if no user is found with the given ID
     */
    @Override
    @Transactional
    public void delete(Long id) {
        User existing = findById(id);
        userRepository.delete(existing);
    }
}
