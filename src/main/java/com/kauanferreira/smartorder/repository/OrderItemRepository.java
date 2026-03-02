package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link OrderItem} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing individual items within customer orders.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all items belonging to a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of items in the given order
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Finds all order items referencing a specific product.
     * Useful for analytics and stock management.
     *
     * @param productId the ID of the product
     * @return a list of order items containing the given product
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Counts how many items are in a specific order.
     *
     * @param orderId the ID of the order
     * @return the number of items in the given order
     */
    long countByOrderId(Long orderId);
}
