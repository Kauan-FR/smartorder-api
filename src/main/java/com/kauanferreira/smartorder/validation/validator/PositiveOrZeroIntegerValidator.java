package com.kauanferreira.smartorder.validation.validator;

import com.kauanferreira.smartorder.validation.PositiveOrZeroInteger;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link PositiveOrZeroInteger} annotation.
 *
 * <p>Checks that the Integer value is not null and is greater than
 * or equal to the configured minimum value.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see PositiveOrZeroInteger
 */
public class PositiveOrZeroIntegerValidator implements ConstraintValidator<PositiveOrZeroInteger, Integer> {

    private String fieldName;

    private int minValue;


    @Override
    public void initialize(PositiveOrZeroInteger constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.minValue = constraintAnnotation.minValue();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            replaceMessage(context, fieldName + " is required");
            return false;
        }
        if (value < minValue) {
            replaceMessage(context, fieldName + " must be at least " + minValue);
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
