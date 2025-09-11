package org.kodelabs.domain.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.kodelabs.domain.common.annotation.ValidEntityField;
import org.kodelabs.domain.common.util.EntityFieldCache;

import java.util.Set;

public class EntityFieldValidator implements ConstraintValidator<ValidEntityField, String> {

    private Class<?> entityClass;

    @Override
    public void initialize(ValidEntityField constraintAnnotation) {
        this.entityClass = constraintAnnotation.entity();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false;
        }

        Set<String> fieldNames = EntityFieldCache.getAllFieldNames(entityClass);
        return fieldNames.contains(value);
    }
}

