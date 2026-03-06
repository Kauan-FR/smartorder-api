package com.kauanferreira.smartorder.validation.validator;

import com.kauanferreira.smartorder.validation.PositiveOrZeroDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;

/**
 * Validator for the {@link PositiveOrZeroDecimal} annotation.
 *
 * <p>Checks that the BigDecimal value is not null and is greater than
 * or equal to zero.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see PositiveOrZeroDecimal
 */
public class PositiveOrZeroDecimalValidator implements ConstraintValidator<PositiveOrZeroDecimal, BigDecimal> {

    private String fieldName;

    @Override
    public void initialize(PositiveOrZeroDecimal constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
    }

    @Override
    public boolean isValid(BigDecimal value, ConstraintValidatorContext context) {
        if (value == null) {
            replaceMessage(context, fieldName + " is required");
            return false;
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            replaceMessage(context, fieldName + " must be zero or positive");
            return false;
        }
        return true;
    }

    /**
     * Replaces the default constraint message with a specific one.
     *
     * @param context the validator context
     * @param message the custom message to set
     */
    private void replaceMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
