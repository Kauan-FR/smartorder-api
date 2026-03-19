package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link OrderItem} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing individual items within customer orders.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product p JOIN FETCH p.category JOIN FETCH oi.order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user")
    List<OrderItem> findAll();

    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product p JOIN FETCH p.category JOIN FETCH oi.order o " +
            "JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user WHERE oi.id = :id")
    Optional<OrderItem> findById(@Param("id") Long id);

    @Query(value = "SELECT oi FROM OrderItem oi JOIN FETCH oi.product p " +
            "JOIN FETCH p.category JOIN FETCH oi.order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user",
            countQuery = "SELECT COUNT(oi) FROM OrderItem oi")
    Page<OrderItem> findAll(Pageable pageable);
    /**
     * Finds all items belonging to a specific order.
     *
     * @param orderId the ID of the order
     * @return a list of items in the given order
     */
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product p JOIN FETCH p.category " +
            "JOIN FETCH oi.order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user WHERE oi.order.id = :orderId")
    List<OrderItem> findByOrderId(@Param("orderId") Long orderId);

    /**
     * Finds all order items referencing a specific product.
     * Useful for analytics and stock management.
     *
     * @param productId the ID of the product
     * @return a list of order items containing the given product
     */
    @Query("SELECT oi FROM OrderItem oi JOIN FETCH oi.product p JOIN FETCH p.category " +
            "JOIN FETCH oi.order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user WHERE oi.product.id = :productId")
    List<OrderItem> findByProductId(@Param("productId") Long productId);

    /**
     * Counts how many items are in a specific order.
     *
     * @param orderId the ID of the order
     * @return the number of items in the given order
     */
    long countByOrderId(Long orderId);
}
