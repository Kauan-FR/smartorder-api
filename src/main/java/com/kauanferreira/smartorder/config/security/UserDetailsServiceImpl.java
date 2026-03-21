package com.kauanferreira.smartorder.config.security;

import com.kauanferreira.smartorder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link UserDetailsService} for Spring Security authentication.
 *
 * <p>Loads user details from the database using the email address
 * as the unique identifier for authentication.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see UserDetailsService
 * @see UserRepository
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Loads a user by their email address for Spring Security authentication.
     *
     * @param email the email address used as username
     * @return the UserDetails of the found user
     * @throws UsernameNotFoundException if no user is found with the given email
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("User not found with email: %s", email)
                ));
    }
}
