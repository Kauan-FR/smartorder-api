package com.kauanferreira.smartorder.services.scheduler;

import com.kauanferreira.smartorder.entity.Order;
import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Scheduled job that advances order statuses to simulate a live order lifecycle.
 *
 * <p>This scheduler exists for portfolio/demo purposes only. It moves orders
 * through the following transitions on a fixed tick interval:</p>
 *
 * <ul>
 *   <li>{@link OrderStatus#PENDING} → {@link OrderStatus#CONFIRMED} after one tick</li>
 *   <li>{@link OrderStatus#CONFIRMED} → {@link OrderStatus#SHIPPED} after two ticks</li>
 * </ul>
 *
 * <p>The transition to {@link OrderStatus#DELIVERED} is intentionally manual —
 * the customer must confirm delivery explicitly from the "My Orders" page.</p>
 *
 * <p>The tick interval is configured via {@code smartorder.demo.order-tick-seconds}
 * in {@code application.yml}.</p>
 *
 * @author Kauan Santos Ferreira
 * @since 2026
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final OrderRepository orderRepository;

    /**
     * Configurable tick interval (in seconds) between status transitions.
     * Defaults to 5 seconds if not set.
     */
    @Value("${smartorder.demo.order-tick-seconds:5}")
    private long tickSeconds;

    /**
     * Polls every second for orders eligible to advance.
     *
     * <p>Runs in its own transaction so that any failure on one tick does not
     * affect subsequent ticks. Uses {@code fixedDelay} so a slow tick will not
     * stack with the next one.</p>
     */
    @Scheduled(fixedDelay = 3000)
    @Transactional
    public void advanceOrderStatuses() {
        advancePending();
        advanceConfirmed();
    }

    /**
     * Advances {@link OrderStatus#PENDING} orders that exceeded one tick interval
     * to {@link OrderStatus#CONFIRMED}.
     */
    private void advancePending() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(tickSeconds);
        List<Order> due = orderRepository.findByStatusAndOrderDateBefore(OrderStatus.PENDING, cutoff);

        for (Order order : due) {
            order.setStatus(OrderStatus.CONFIRMED);
            orderRepository.save(order);
            log.info("Order {} advanced PENDING -> CONFIRMED", order.getId());
        }
    }

    /**
     * Advances {@link OrderStatus#CONFIRMED} orders that exceeded two tick intervals
     * (from creation) to {@link OrderStatus#SHIPPED}.
     */
    private void advanceConfirmed() {
        LocalDateTime cutoff = LocalDateTime.now().minusSeconds(tickSeconds * 2);
        List<Order> due = orderRepository.findByStatusAndOrderDateBefore(OrderStatus.CONFIRMED, cutoff);

        for (Order order : due) {
            order.setStatus(OrderStatus.SHIPPED);
            orderRepository.save(order);
            log.info("Order {} advanced CONFIRMED -> SHIPPED", order.getId());
        }
    }
}