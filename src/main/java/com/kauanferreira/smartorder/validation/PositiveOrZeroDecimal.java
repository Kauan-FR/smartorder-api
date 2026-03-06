package com.kauanferreira.smartorder.validation;

import com.kauanferreira.smartorder.validation.validator.PositiveOrZeroDecimalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for monetary fields using {@link java.math.BigDecimal}.
 *
 * <p>Validates that the value is not null and is zero or positive.
 * Designed for price, salary, and total amount fields.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see PositiveOrZeroDecimalValidator
 */
@Documented
@Constraint(validatedBy = {PositiveOrZeroDecimalValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveOrZeroDecimal {

    String message() default "Value must be zero or positive";

    String fieldName() default "Value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
