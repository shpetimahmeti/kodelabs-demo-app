package org.kodelabs.domain.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.kodelabs.domain.common.entity.BaseEntity;
import org.kodelabs.domain.common.validator.SortFieldValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SortFieldValidator.class)
public @interface ValidSortField {
    String message() default "Invalid sorting field";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    Class<? extends BaseEntity> entity(); // required to pass the entity
}