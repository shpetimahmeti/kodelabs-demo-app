package org.kodelabs.domain.common.util;

import org.bson.codecs.pojo.annotations.BsonProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class EntityFieldCache {

    private static final Map<Class<?>, Set<String>> CACHE = new ConcurrentHashMap<>();

    private EntityFieldCache() {
    }

    /**
     * Returns all accessible Mongo field names for the given entity class (including inherited fields).
     * - Uses @BsonProperty value if present, otherwise the Java field name.
     * - Skips static and transient fields.
     * - Skips private fields that have no public getter.
     * Results are cached per class.
     */
    public static Set<String> getAllFieldNames(Class<?> entityClass) {
        return CACHE.computeIfAbsent(entityClass, EntityFieldCache::resolveFieldNames);
    }

    private static Set<String> resolveFieldNames(Class<?> entityClass) {
        LinkedHashSet<String> fieldNames = new LinkedHashSet<>();

        Class<?> current = entityClass;
        while (current != null && current != Object.class) {
            for (Field field : current.getDeclaredFields()) {
                int mods = field.getModifiers();
                if (Modifier.isStatic(mods) || Modifier.isTransient(mods)) {
                    continue;
                }

                if (Modifier.isPrivate(mods) && !hasPublicGetter(field)) {
                    continue;
                }

                fieldNames.add(resolveMongoFieldName(field));
            }
            current = current.getSuperclass();
        }

        return Collections.unmodifiableSet(fieldNames);
    }

    private static boolean hasPublicGetter(Field field) {
        Class<?> declaring = field.getDeclaringClass();
        String name = field.getName();
        String capitalized = capitalize(name);


        try {
            Method m = declaring.getMethod("get" + capitalized);
            if (m.getParameterCount() == 0 && Modifier.isPublic(m.getModifiers())) {
                return true;
            }
        } catch (NoSuchMethodException ignored) {
        }

        try {
            Method m = declaring.getMethod("is" + capitalized);
            if (m.getParameterCount() == 0 && Modifier.isPublic(m.getModifiers())) {
                Class<?> rt = m.getReturnType();
                if (rt == boolean.class || rt == Boolean.class) {
                    return true;
                }
            }
        } catch (NoSuchMethodException ignored) {
        }

        return false;
    }

    private static String resolveMongoFieldName(Field field) {
        BsonProperty bp = field.getAnnotation(BsonProperty.class);
        if (bp != null && bp.value() != null && !bp.value().isEmpty()) {
            return bp.value();
        }
        return field.getName();
    }

    private static String capitalize(String s) {
        if (s == null || s.isEmpty()) return s;
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}
