package com.kauanferreira.smartorder.enums;

/**
 * Enum representing the possible statuses of an order
 * throughout its lifecycle in the SmartOrder system.
 *
 * @author Kauan
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
