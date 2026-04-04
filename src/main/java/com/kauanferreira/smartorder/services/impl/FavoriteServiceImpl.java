package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Favorite;
import com.kauanferreira.smartorder.entity.Product;
import com.kauanferreira.smartorder.entity.User;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.FavoriteRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.FavoriteService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link FavoriteService}.
 *
 * <p>Handles business logic for favorites/wishlist management,
 * including duplicate prevention and ownership validation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see FavoriteService
 * @see FavoriteRepository
 */
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Favorite> getFavorites(String email) {
        User user = findUserByEmail(email);
        return favoriteRepository.findByUserId(user.getId());
    }

    /**
     * {@inheritDoc}
     *
     * <p>If the product is already favorited, returns the existing favorite
     * without creating a duplicate.</p>
     *
     * @throws ResourceNotFoundException if the product does not exist
     */
    @Override
    @Transactional
    public Favorite addFavorite(String email, Long productId) {
        User user = findUserByEmail(email);
        productService.findById(productId);

        Optional<Favorite> existing = favoriteRepository
                .findByUserIdAndProductId(user.getId(), productId);

        if (existing.isPresent()) {
            return existing.get();
        }

        Favorite favorite = new Favorite();
        favorite.setUser(user);

        Product product = new Product();
        product.setId(productId);
        favorite.setProduct(product);

        Favorite saved = favoriteRepository.save(favorite);
        entityManager.flush();
        entityManager.clear();
        return favoriteRepository.findById(saved.getId()).orElse(saved);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the product is not in the user's favorites
     */
    @Override
    @Transactional
    public void removeFavorite(String email, Long productId) {
        User user = findUserByEmail(email);
        favoriteRepository.findByUserIdAndProductId(user.getId(), productId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Product with ID %d is not in your favorites", productId)));
        favoriteRepository.deleteByUserIdAndProductId(user.getId(), productId);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean isFavorited(String email, Long productId) {
        User user = findUserByEmail(email);
        return favoriteRepository.findByUserIdAndProductId(user.getId(), productId).isPresent();
    }

    /**
     * Finds a user by email or throws an exception.
     *
     * @param email the user's email
     * @return the found user
     * @throws ResourceNotFoundException if no user is found with the given email
     */
    private User findUserByEmail(String email) {
        return userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("User with email '%s' not found", email)));
    }
}