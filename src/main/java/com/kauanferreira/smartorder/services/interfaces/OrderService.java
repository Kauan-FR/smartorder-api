package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for {@link Order} entity operations.
 *
 * <p>Provides CRUD operations and query methods for managing
 * orders in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface OrderService {

    /**
     * Creates a new order for an existing user and address.
     *
     * @param order the order entity to persist
     * @return the persisted order with generated id
     */
    Order create(Order order);


    /**
     * Finds an order by its unique identifier.
     *
     * @param id the order id
     * @return the found order
     * @throws jakarta.persistence.EntityNotFoundException if no order is found with the given id
     */
    Order findById(Long id);

    /**
     * Retrieves all orders.
     *
     * @return list of all orders, or empty list if none exist
     */
    List<Order> findAll();

    /**
     * Retrieves all orders with pagination support.
     *
     * @param pageable pagination parameters
     * @return a page of orders
     */
    Page<Order> findAll(Pageable pageable);

    /**
     * Retrieves all orders belonging to a specific user, ordered by date descending.
     *
     * @param userId the user id
     * @return list of orders for the given user, or empty list if none exist
     */
    List<Order> findByUserId(Long userId);

    /**
     * Retrieves all orders with a specific status.
     *
     * @param status the order status
     * @return list of matching orders, or empty list if none match
     */
    List<Order> findByStatus(OrderStatus status);

    /**
     * Retrieves all orders belonging to a specific user with a specific status.
     *
     * @param userId the user id
     * @param status the order status
     * @return list of matching orders, or empty list if none match
     */
    List<Order> findByUserIdAndStatus(Long userId, OrderStatus status);

    /**
     * Retrieves all orders associated with a specific address.
     *
     * @param addressId the address id
     * @return list of matching orders, or empty list if none match
     */
    List<Order> findByAddressId(Long addressId);

    /**
     * Counts the total number of orders for a specific user.
     *
     * @param userId the user id
     * @return the order count for the given user
     */
    long countByUserId(Long userId);

    /**
     * Counts the total number of orders with a specific status.
     *
     * @param status the order status
     * @return the order count for the given status
     */
    long countByStatus(OrderStatus status);

    /**
     * Updates the status of an existing order.
     *
     * @param id the id of the order to update
     * @param status the new order status
     * @return the updated order
     */
    Order updateStatus(Long id, OrderStatus status);

    /**
     * Updates an existing order.
     *
     * @param id the id of the order to update
     * @param order the order data with updated fields
     * @return the updated order
     */
    Order update(Long id, Order order);

    /**
     * Returns true if the user has at least one OrderItem referencing the given
     * product across orders that are not in CANCELLED status.
     */
    boolean hasUserPurchasedProduct(String email, Long productId);

    /**
     * Deletes an order by its id.
     *
     * @param id the id of the order to delete
     * @throws jakarta.persistence.EntityNotFoundException if no order is found with the given id
     */
    void delete(Long id);
}
