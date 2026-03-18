package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.OrderRepository;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link OrderService} providing CRUD operations
 * and query methods for managing orders.
 *
 * <p>Validates user and address existence through {@link UserService}
 * and {@link AddressService} before creating or updating orders.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderService
 * @see OrderRepository
 * @see UserService
 * @see AddressService
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final AddressService addressService;

    private final UserService userService;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the associated user or address does not exist
     */
    @Override
    @Transactional
    public Order create(Order order) {
        var user = userService.findById(order.getUser().getId());
        var address = addressService.findById(order.getAddress().getId());
        order.setUser(user);
        order.setAddress(address);
        return orderRepository.save(order);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no order is found with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Order with id %d not found", id))
        );
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserId(Long userId) {
        userService.findById(userId);
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    /** {@inheritDoc} */

    @Override
    @Transactional(readOnly = true)
    public List<Order> findByStatus(OrderStatus status) {
        return orderRepository.findByStatus(status);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByUserIdAndStatus(Long userId, OrderStatus status) {
        userService.findById(userId);
        return orderRepository.findByUserIdAndStatus(userId, status);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<Order> findByAddressId(Long addressId) {
        return orderRepository.findByAddressId(addressId);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long countByUserId(Long userId) {
        return orderRepository.countByUserId(userId);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long countByStatus(OrderStatus status) {
        return orderRepository.countByStatus(status);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no order is found with the given id
     */
    @Override
    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order existing = findById(id);
        existing.setStatus(status);
        return orderRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Updates the following fields: status, total amount, and address.
     * The order date is not updatable as it is set on creation.</p>
     *
     * @throws ResourceNotFoundException if no order is found with the given id
     *         or if the new address does not exist
     */
    @Override
    @Transactional
    public Order update(Long id, Order order) {
        Order existing = findById(id);
        var address = addressService.findById(order.getAddress().getId());

        existing.setStatus(order.getStatus());
        existing.setTotalAmount(order.getTotalAmount());
        existing.setAddress(address);
        return orderRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no order is found with the given id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }
}
