package com.kauanferreira.smartorder.validation;

import com.kauanferreira.smartorder.validation.validator.PositiveOrZeroIntegerValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.hibernate.validator.internal.constraintvalidators.bv.number.sign.PositiveOrZeroValidatorForBigInteger;

import java.lang.annotation.*;

/**
 * Custom validation annotation for required Integer fields that must be zero or positive.
 *
 * <p>Combines not-null and minimum value validations into a single reusable annotation.
 * Designed for fields like stock quantity and item quantity.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see PositiveOrZeroIntegerValidator
 */
@Documented
@Constraint(validatedBy = PositiveOrZeroIntegerValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveOrZeroInteger {

    String message() default "Value must be zero or positive";

    String fieldName() default "value";

    int minValue() default 0;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
