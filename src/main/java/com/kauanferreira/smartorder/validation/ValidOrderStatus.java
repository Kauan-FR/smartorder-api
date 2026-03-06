package com.kauanferreira.smartorder.validation;

import com.kauanferreira.smartorder.validation.validator.ValidOrderStatusValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for {@link com.kauanferreira.smartorder.enums.OrderStatus} fields.
 *
 * <p>Validates that the status is not null and is a recognized enum value.
 * Provides a clear error message listing all accepted values.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ValidOrderStatusValidator
 */
@Documented
@Constraint(validatedBy = ValidOrderStatusValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOrderStatus {

    String message() default "Invalid order status. Accepted values: PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
