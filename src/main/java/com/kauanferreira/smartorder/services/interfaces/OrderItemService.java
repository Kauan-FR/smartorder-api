package com.kauanferreira.smartorder.services.interfaces;

import com.kauanferreira.smartorder.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service interface for {@link OrderItem} entity operations.
 *
 * <p>Provides CRUD operations and query methods for managing
 * order items in the SmartOrder system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public interface OrderItemService {

    /**
     * Creates a new order item for an existing order and product.
     *
     * @param orderItem the order item entity to persist
     * @return the persisted order item with generated id
     */
    OrderItem create(OrderItem orderItem);

    /**
     * Finds an order item by its unique identifier.
     *
     * @param id the order item id
     * @return the found order item
     */
    OrderItem findById(Long id);

    /**
     * Retrieves all order items.
     *
     * @return list of all order items, or empty list if none exist
     */
    List<OrderItem> findAll();

    /**
     * Retrieves all order items with pagination support.
     *
     * @param pageable pagination parameters
     * @return a page of order items
     */
    Page<OrderItem> findAll(Pageable pageable);

    /**
     * Retrieves all items belonging to a specific order.
     *
     * @param orderId the order id
     * @return list of items for the given order, or empty list if none exist
     */
    List<OrderItem> findByOrderId(Long orderId);

    /**
     * Retrieves all order items referencing a specific product.
     *
     * @param productId the product id
     * @return list of items for the given product, or empty list if none exist
     */
    List<OrderItem> findByProductId(Long productId);

    /**
     * Counts the total number of items in a specific order.
     *
     * @param orderId the order id
     * @return the item count for the given order
     */
    long countByOrderId(Long orderId);

    /**
     * Updates an existing order item.
     *
     * @param id the id of the order item to update
     * @param orderItem the order item data with updated fields
     * @return the updated order item
     */
    OrderItem update(Long id, OrderItem orderItem);

    /**
     * Deletes an order item by its id.
     *
     * @param id the id of the order item to delete
     * @throws jakarta.persistence.EntityNotFoundException if no order item is found with the given id
     */
    void delete(Long id);
}
