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

    protected final ReactiveMongoCollection<T> collection;
    protected final Class<F> facetResultClass;

    protected BaseRepository() {
        this.facetResultClass = null;
        this.collection = null;
    }

    protected BaseRepository(ReactiveMongoCollection<T> collection, Class<F> facetResultClass) {
        this.collection = collection;
        this.facetResultClass = facetResultClass;
    }

    protected Uni<F> loadPaginationFacetResult(
            int page,
            int size,
            String sortField,
            boolean ascending
    ) {
        List<Bson> pipeline = List.of(
                Aggregates.facet(
                        new Facet("items",
                                Aggregates.sort(ascending ? Sorts.ascending(sortField) : Sorts.descending(sortField)),
                                Aggregates.skip(page * size),
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
