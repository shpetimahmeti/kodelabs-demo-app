package org.kodelabs.domain.common.mongo;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.kodelabs.domain.common.annotation.SortableField;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.kodelabs.domain.common.mongo.Fields.ID;

public final class EntityFieldCache {

    private static final Map<Class<?>, Set<String>> CACHE = new ConcurrentHashMap<>();

    private EntityFieldCache() {
    }

    public static Set<String> getSortableFieldNames(Class<?> entityClass) {
        return CACHE.computeIfAbsent(entityClass, EntityFieldCache::resolveSortableFields);
    }

    private static Set<String> resolveSortableFields(Class<?> entityClass) {
        LinkedHashSet<String> fieldNames = new LinkedHashSet<>();
        Class<?> current = entityClass;

        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                if (!field.isAnnotationPresent(SortableField.class)) continue;

                fieldNames.add(resolveMongoFieldName(field));
            }
            current = current.getSuperclass();
        }

        return Collections.unmodifiableSet(fieldNames);
    }

    private static String resolveMongoFieldName(Field field) {
        BsonProperty bp = field.getAnnotation(BsonProperty.class);

        if (bp != null && !bp.value().isEmpty()) {
            return bp.value();
        }

        BsonId id = field.getAnnotation(BsonId.class);
        if (id != null) {
            return ID;
        }

        return field.getName();
    }
}
