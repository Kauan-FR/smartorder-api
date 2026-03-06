package com.kauanferreira.smartorder.validation.validator;

import com.kauanferreira.smartorder.validation.NotBlankField;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator for the {@link NotBlankField} annotation.
 *
 * <p>Checks that the String value is not null, not blank, and does not
 * exceed the configured maximum length.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see NotBlankField
 */
public class NotBlankFieldValidator implements ConstraintValidator<NotBlankField, String> {

    private String fieldName;

    private int maxLength;


    @Override
    public void initialize(NotBlankField constraintAnnotation) {
        this.fieldName = constraintAnnotation.fieldName();
        this.maxLength = constraintAnnotation.maxLength();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            replaceMessage(context, fieldName + " is required");
            return false;
        }
        if (value.length() > maxLength) {
            replaceMessage(context, fieldName + " must not exceed " + maxLength + " characters");
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
