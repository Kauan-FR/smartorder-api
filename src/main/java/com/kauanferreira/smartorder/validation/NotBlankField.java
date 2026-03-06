package com.kauanferreira.smartorder.validation;

import com.kauanferreira.smartorder.validation.validator.NotBlankFieldValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom validation annotation for required String fields with a maximum length.
 *
 * <p>Combines not-blank and size validations into a single reusable annotation
 * with a configurable field name for clear error messages.</p>
 *
 * @author Kauan Santos Ferreira
 * @version 1.0
 * @since 2026
 * @see NotBlankFieldValidator
 */
@Documented
@Constraint(validatedBy = NotBlankFieldValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotBlankField {

    String message() default "Field is required";

    String fieldName() default "Field";

    int maxLength() default 255;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
