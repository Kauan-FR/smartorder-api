package com.kauanferreira.smartorder.repository;

import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for {@link Order} entity.
 *
 * <p>Provides CRUD operations and custom query methods
 * for managing customer orders in the e-commerce system.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category")
    List<Order> findAll();

    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE o.id = :id")
    Optional<Order> findById(@Param("id") Long id);

    @Query(value = "SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category",
            countQuery = "SELECT COUNT(o) FROM Order o")
    Page<Order> findAll(Pageable pageable);

    /**
     * Finds all orders placed by a specific user, ordered by date descending.
     *
     * @param userId the ID of the user
     * @return a list of orders for the given user (most recent first)
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE o.user.id = :userId ORDER BY o.orderDate DESC")
    List<Order> findByUserIdOrderByOrderDateDesc(@Param("userId") Long userId);

    /**
     * Finds all orders with a specific status.
     *
     * @param status the order status to filter by
     * @return a list of orders with the given status
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE o.status = :status")
    List<Order> findByStatus(@Param("status") OrderStatus status);

    /**
     * Finds all orders for a specific user with a specific status.
     *
     * @param userId the ID of the user
     * @param status the order status to filter by
     * @return a list of matching orders
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE o.user.id = :userId AND o.status = :status")
    List<Order> findByUserIdAndStatus(@Param("userId") Long userId, @Param("status") OrderStatus status);

    /**
     * Finds all orders shipped to a specific address.
     *
     * @param addressId the ID of the address
     * @return a list of orders for the given address
     */
    @Query("SELECT o FROM Order o JOIN FETCH o.user JOIN FETCH o.address a JOIN FETCH a.user LEFT JOIN FETCH " +
            "o.items oi LEFT JOIN FETCH oi.product p LEFT JOIN FETCH p.category WHERE o.address.id = :addressId")
    List<Order> findByAddressId(@Param("addressId") Long addressId);

    @Query("""
    SELECT COUNT(oi) > 0 FROM Order o
    JOIN o.items oi
    WHERE o.user.id = :userId
      AND oi.product.id = :productId
      AND o.status <> :status
""")
    boolean existsByUserIdAndProductIdAndStatusNot(@Param("userId") Long userId,
                                                   @Param("productId") Long productId,
                                                   @Param("status") OrderStatus status);

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
