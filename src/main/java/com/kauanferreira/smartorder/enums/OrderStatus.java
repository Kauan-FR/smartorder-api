package com.kauanferreira.smartorder.enums;

/**
 * Enum representing the possible statuses of an order
 * throughout its lifecycle in the SmartOrder system.
 *
 * <ul>
 *   <li>{@link #PENDING} — Order created, awaiting confirmation.</li>
 *   <li>{@link #CONFIRMED} — Order confirmed and being prepared.</li>
 *   <li>{@link #SHIPPED} — Order dispatched for delivery.</li>
 *   <li>{@link #DELIVERED} — Order successfully delivered to the customer.</li>
 *   <li>{@link #CANCELLED} — Order cancelled before delivery.</li>
 * </ul>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 */
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED,
    CANCELLED
}
