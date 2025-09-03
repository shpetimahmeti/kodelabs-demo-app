package org.kodelabs.domain.common;

import com.mongodb.client.model.Filters;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import org.bson.conversions.Bson;

import java.time.Instant;

public class MongoHelper {

    public static <T extends BaseEntity> Uni<Void> insert(T entity, ReactiveMongoCollection<T> collection) {
        entity.generateId();

        entity.createdAt = Instant.now();
        entity.updatedAt = entity.createdAt;

        return collection.insertOne(entity).onItem().ignore().andContinueWithNull();
    }

    public static <T extends BaseEntity> Uni<T> insertAndReturn(T entity, ReactiveMongoCollection<T> collection) {
        entity.generateId();
        Instant now = Instant.now();
        entity.createdAt = now;
        entity.updatedAt = now;

        return collection.insertOne(entity)
                .onItem().transform(insertOneResult -> entity);
    }

    public static <T extends BaseEntity> Uni<Void> update(T entity, ReactiveMongoCollection<T> collection) {
        entity.updatedAt = Instant.now();
        Bson filter = Filters.eq("_id", entity.getId());

        return collection.replaceOne(filter, entity)
                .onItem().ignore().andContinueWithNull();
    }

    public static <T extends BaseEntity> Uni<T> updateAndReturn(T entity, ReactiveMongoCollection<T> collection) {
        entity.updatedAt = Instant.now();
        Bson filter = Filters.eq("_id", entity.getId());

        return collection.replaceOne(filter, entity)
                .onItem().transform(updateResult -> entity);
    }
}
