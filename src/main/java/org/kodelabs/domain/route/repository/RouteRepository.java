package org.kodelabs.domain.route.repository;

import com.mongodb.client.model.Filters;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.route.entity.RouteEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;


import static com.mongodb.client.model.Aggregates.match;
import static com.mongodb.client.model.Filters.eq;

@ApplicationScoped
public class RouteRepository {

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.route-collection")
    String routeCollection;

    private final String SEGMENTS_FIELD = "segments";
    private final String FROM_FIELD = "from";
    private final String TO_FIELD = "to";
    private final String DATE_FIELD = "date";

    public Multi<RouteEntity> getAllRoutes(String originIata, String destinationIata, LocalDate localDate) {
        ReactiveMongoCollection<RouteEntity> collection = getCollection();

        List<Bson> filters = new ArrayList<>();

        Bson segmentFilter = Filters.elemMatch(SEGMENTS_FIELD,
                Filters.and(
                        Filters.eq(FROM_FIELD, originIata),
                        Filters.eq(TO_FIELD, destinationIata)
                )
        );

        filters.add(segmentFilter);

        if (localDate != null) {
            Instant start = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant end = localDate.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

            filters.add(Filters.gte(DATE_FIELD, start));
            filters.add(Filters.lt(DATE_FIELD, end));
        }

        List<Bson> pipeline = List.of(match(Filters.and(filters)));

        return collection.aggregate(pipeline, RouteEntity.class);
    }

    private ReactiveMongoCollection<RouteEntity> getCollection() {
        return client.getDatabase(database)
                .getCollection(routeCollection, RouteEntity.class);
    }
}
