package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Order} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing customer orders in the e-commerce system.</p>
 *
 * @author Kauan
 * @version 1.0
 * @since 2026
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Finds all orders placed by a specific user, ordered by date descending.
     *
     * @param userId the ID of the user
     * @return a list of orders for the given user (most recent first)
     */
    List<Order> findByUserIdOrderByOrderDateDesc(Long userId);

    /**
     * Finds all orders with a specific status.
     *
     * @param status the order status to filter by
     * @return a list of orders with the given status
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Finds all orders for a specific user with a specific status.
     *
     * @param userId the ID of the user
     * @param status the order status to filter by
     * @return a list of matching orders
     */
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Finds all orders shipped to a specific address.
     *
     * @param addressId the ID of the address
     * @return a list of orders for the given address
     */
    List<Order> findByAddressId(Long addressId);

    /**
     * Counts how many orders a user has placed.
     *
     * @param userId the ID of the user
     * @return the number of orders for the given user
     */
    long countByUserId(Long userId);

    /**
     * Counts how many orders have a specific status.
     *
     * @param status the order status to count
     * @return the number of orders with the given status
     */
    long countByStatus(OrderStatus status);
}
