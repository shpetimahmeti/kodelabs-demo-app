package org.kodelabs.domain.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.kodelabs.domain.common.validator.EntityFieldValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EntityFieldValidator.class)
@Documented
public @interface ValidEntityField {
    String message() default "Invalid field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<?> entity();
}
