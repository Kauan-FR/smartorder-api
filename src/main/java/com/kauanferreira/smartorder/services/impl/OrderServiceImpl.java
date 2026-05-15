package com.kauanferreira.smartorder.services.impl;

import com.kauanferreira.smartorder.dto.request.CheckoutRequest;
import com.kauanferreira.smartorder.entity.*;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.exception.ResourceNotFoundException;
import com.kauanferreira.smartorder.repository.OrderRepository;
import com.kauanferreira.smartorder.repository.UserRepository;
import com.kauanferreira.smartorder.services.interfaces.AddressService;
import com.kauanferreira.smartorder.services.interfaces.OrderService;
import com.kauanferreira.smartorder.services.interfaces.ProductService;
import com.kauanferreira.smartorder.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    private final UserRepository userRepository;
    private final ProductService productService;

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
    public List<Order> findByAuthenticatedUser(String email) {
        User user = userService.findByEmail(email);
        return orderRepository.findByUserIdOrderByOrderDateDesc(user.getId());
    }

    /**
     * {@inheritDoc}
     *
     * <p>Implementation details:</p>
     * <ul>
     *   <li>Resolves the user by email (from JWT) — userId is never trusted from the client.</li>
     *   <li>Picks the user's first registered address. Throws if none exists.</li>
     *   <li>Validates and decrements product stock via {@link ProductService#decreaseStock}.</li>
     *   <li>Calculates the final unit price (applying discount if active) and the subtotal.</li>
     *   <li>Persists the order with one {@link OrderItem} attached via cascade.</li>
     * </ul>
     *
     * @throws ResourceNotFoundException    if the user, product, or address is not found
     * @throws IllegalStateException        if the user has no registered address
     */
    @Override
    @Transactional
    public Order checkout(String email, CheckoutRequest request) {
        User user = userService.findByEmail(email);

        List<Address> userAddresses = addressService.findByUserId(user.getId());
        if (userAddresses.isEmpty()) {
            throw new IllegalStateException(
                    "User must register a delivery address before placing an order"
            );
        }
        Address address = userAddresses.get(0);

        Product product = productService.findById(request.productId());
        productService.decreaseStock(product.getId(), request.quantity());

        BigDecimal unitPrice = calculateUnitPrice(product);
        BigDecimal subtotal = unitPrice.multiply(BigDecimal.valueOf(request.quantity()));

        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(subtotal);

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQuantity(request.quantity());
        item.setPrice(unitPrice);
        item.setSubtotal(subtotal);

        order.getItems().add(item);

        return orderRepository.save(order);
    }

    /**
     * Calculates the unit price applying the active discount if present and not expired.
     *
     * @param product the product to price
     * @return the final unit price after any active discount
     */
    private BigDecimal calculateUnitPrice(Product product) {
        Integer discount = product.getDiscountPercent();
        if (discount == null || discount <= 0) {
            return product.getPrice();
        }

        if (product.getDealExpiresAt() != null
                && product.getDealExpiresAt().isBefore(java.time.LocalDateTime.now())) {
            return product.getPrice();
        }

        BigDecimal discountFactor = BigDecimal.valueOf(100 - discount)
                .divide(BigDecimal.valueOf(100));
        return product.getPrice().multiply(discountFactor);
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
     * <p>If the new status is {@link OrderStatus#CANCELLED} and the order was not
     * already cancelled, stock is returned to inventory for every item in the order.
     * This prevents stock leakage when customers cancel pending or confirmed orders.</p>
     *
     * @throws ResourceNotFoundException if no order is found with the given id
     */
    @Override
    @Transactional
    public Order updateStatus(Long id, OrderStatus status) {
        Order existing = findById(id);
        OrderStatus previousStatus = existing.getStatus();

        if (status == OrderStatus.CANCELLED && previousStatus != OrderStatus.CANCELLED) {
            restoreStock(existing);
        }

        existing.setStatus(status);
        return orderRepository.save(existing);
    }

    /**
     * Returns the stock of every item in the order back to inventory.
     * Called when an order is transitioned to {@link OrderStatus#CANCELLED}.
     *
     * @param order the order whose items will have their stock restored
     */
    private void restoreStock(Order order) {
        for (OrderItem item : order.getItems()) {
            productService.increaseStock(item.getProduct().getId(), item.getQuantity());
        }
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
     * @throws ResourceNotFoundException if the user does not exist
     */
    @Override
    public boolean hasUserPurchasedProduct(String email, Long productId) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.existsByUserIdAndProductIdAndStatusNot(
                user.getId(), productId, OrderStatus.CANCELLED
        );
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
