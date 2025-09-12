package org.kodelabs.domain.common.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.kodelabs.domain.common.annotation.ValidSortField;
import org.kodelabs.domain.common.dto.PaginationQueryParams;
import org.kodelabs.domain.common.entity.BaseEntity;
import org.kodelabs.domain.common.mongo.EntityFieldCache;

import java.util.Set;

public class SortFieldValidator implements ConstraintValidator<ValidSortField, PaginationQueryParams> {

    private Class<? extends BaseEntity> entityClass;

    @Override
    public void initialize(ValidSortField annotation) {
        this.entityClass = annotation.entity();
    }

    @Override
    public boolean isValid(PaginationQueryParams params, ConstraintValidatorContext context) {
        if (params.sortField == null || params.sortField.isBlank()) {
            return false;
        }

        Set<String> sortableFields = EntityFieldCache.getSortableFieldNames(entityClass);
        return sortableFields.contains(params.sortField);
    }
}

