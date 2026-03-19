package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.entity.OrderItem;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.OrderItemRepository;
import com.kauanferreira.smartorder.services.interfaces.OrderItemService;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link OrderItemService} providing CRUD operations
 * and query methods for managing order items.
 *
 * <p>Validates order and product existence through {@link OrderService}
 * and {@link ProductService} before creating or updating order items.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see OrderItemService
 * @see OrderItemRepository
 * @see OrderService
 * @see ProductService
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final OrderService orderService;

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the associated order or product does not exist
     */
    @Override
    @Transactional
    public OrderItem create(OrderItem orderItem) {
        var order = orderService.findById(orderItem.getOrder().getId());
        var product = productService.findById(orderItem.getProduct().getId());
        orderItem.setOrder(order);
        orderItem.setProduct(product);
        return orderItemRepository.save(orderItem);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no order item is found with the given id
     */
    @Override
    @Transactional(readOnly = true)
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("OrderItem with id %d not found", id))
        );
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findAll() {
        return orderItemRepository.findAll();
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItem> findAll(Pageable pageable) {
        return orderItemRepository.findAll(pageable);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the order does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByOrderId(Long orderId) {
        orderService.findById(orderId);
        return orderItemRepository.findByOrderId(orderId);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if the product does not exist
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrderItem> findByProductId(Long productId) {
        productService.findById(productId);
        return orderItemRepository.findByProductId(productId);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long countByOrderId(Long orderId) {
        return orderItemRepository.countByOrderId(orderId);
    }

    /**
     * {@inheritDoc}
     *
     * <p>Updates the following fields: quantity, price, subtotal, and product.
     * The order association is not updatable.</p>
     *
     * @throws ResourceNotFoundException if no order item is found with the given id
     *         or if the new product does not exist
     */
    @Override
    @Transactional
    public OrderItem update(Long id, OrderItem orderItem) {
        OrderItem existing = findById(id);
        productService.findById(orderItem.getProduct().getId());

        existing.setQuantity(orderItem.getQuantity());
        existing.setPrice(orderItem.getPrice());
        existing.setSubtotal(orderItem.getSubtotal());
        existing.setProduct(orderItem.getProduct());
        return  orderItemRepository.save(existing);
    }

    /**
     * {@inheritDoc}
     *
     * @throws ResourceNotFoundException if no order item is found with the given id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        OrderItem existing = findById(id);
        orderItemRepository.delete(existing);
    }
}
