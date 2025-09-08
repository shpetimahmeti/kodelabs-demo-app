package org.kodelabs.domain.common.repository;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.kodelabs.domain.common.dto.PaginationFacetResult;

import java.util.List;

public abstract class BaseRepository<T, F extends PaginationFacetResult<T>> {


    protected Uni<F> loadPaginationFacetResult(
            ReactiveMongoCollection<T> collection,
            Class<F> facetResultClass,
            int page,
            int size,
            String sortField,
            boolean ascending
    ) {
        if (page < 0) {
            return Uni.createFrom().failure(new IllegalArgumentException("page must be >= 0"));
        }
        if (size <= 0) {
            return Uni.createFrom().failure(new IllegalArgumentException("size must be > 0"));
        }

        int skip = page * size;

        List<Bson> pipeline = List.of(
                Aggregates.facet(
                        new Facet("items",
                                Aggregates.sort(ascending ? Sorts.ascending(sortField) : Sorts.descending(sortField)),
                                Aggregates.skip(skip),
                                Aggregates.limit(size)
                        ),
                        new Facet("totalCount",
                                Aggregates.count("count")
                        )
                ),
                Aggregates.project(
                        new Document("items", 1)
                                .append("totalCount", new Document("$arrayElemAt", List.of("$totalCount.count", 0)))
                )
        );

        return Multi.createFrom()
                .publisher(collection.aggregate(pipeline, facetResultClass))
                .collect().first();
    }
}
