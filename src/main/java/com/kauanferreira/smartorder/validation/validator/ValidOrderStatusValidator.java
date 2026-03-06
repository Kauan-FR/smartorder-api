package com.kauanferreira.smartorder.validation.validator;

import com.kauanferreira.smartorder.enums.OrderStatus;
import com.kauanferreira.smartorder.validation.ValidOrderStatus;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Validator for the {@link ValidOrderStatus} annotation.
 *
 * <p>Checks that the OrderStatus value is not null and belongs
 * to the set of recognized enum constants.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ValidOrderStatus
 */
public class ValidOrderStatusValidator implements ConstraintValidator<ValidOrderStatus, OrderStatus> {

    public static final Set<OrderStatus> VALID_STATUSES =
            Arrays.stream(OrderStatus.values()).collect(Collectors.toSet());

    @Override
    public boolean isValid(OrderStatus value, ConstraintValidatorContext context) {
        if (value == null) {
            replaceMessage(context, "Order status is required");
            return false;
        }
        if (!VALID_STATUSES.contains(value)) {
            replaceMessage(context, "Invalid order status. Accepted values: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED");
            return false;
        }
        return true;
    }

    private void replaceMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
