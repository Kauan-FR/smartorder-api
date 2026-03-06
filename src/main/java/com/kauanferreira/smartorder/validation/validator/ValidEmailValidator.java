package com.kauanferreira.smartorder.validation.validator;

import com.kauanferreira.smartorder.validation.ValidEmail;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}$");

    private static final int MAX_LENGTH = 150;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            replaceMessage(context, "Email is required");
            return false;
        }
        if (value.length() > MAX_LENGTH) {
            replaceMessage(context, "Email must not exceed 150 characters");
            return  false;
        }
        if (!EMAIL_PATTERN.matcher(value).matches()) {
            replaceMessage(context, "Email format is invalid");
            return  false;
        }
        return true;
    }

    /**
     * Replaces the default constraint message with a specific one.
     *
     * @param context the validator context
     * @param message the custom message to set
     */
    private void  replaceMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
