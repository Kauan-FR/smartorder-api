package com.kauanferreira.smartorder.validation;

import com.kauanferreira.smartorder.validation.validator.ValidEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Custom validation annotation for email fields.
 *
 * <p>Validates that the email is not blank, has a valid format,
 * and does not exceed 150 characters. Combines multiple standard
 * validations into a single reusable annotation.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see ValidEmailValidator
 */
@Documented
@Constraint(validatedBy = {ValidEmailValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface ValidEmail {

    String message() default "Email must be valid and not exceed 150 characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
