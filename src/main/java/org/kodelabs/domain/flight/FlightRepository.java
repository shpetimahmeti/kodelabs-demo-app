package org.kodelabs.domain.flight;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.flight.dto.FlightFacetResult;

import java.util.List;

@ApplicationScoped
public class FlightRepository {

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.flight-collection")
    String flightCollection;

    private final String DATE_FIELD = "date";

    public Multi<FlightFacetResult> findFlight(String originIata,
                                               String destinationIata,
                                               int page,
                                               int size) {
        ReactiveMongoCollection<FlightFacetResult> collection = client.getDatabase(database)
                .getCollection(flightCollection, FlightFacetResult.class);


        List<Bson> pipeline = List.of(
                Aggregates.match(
                        Filters.and(
                         Filters.eq("origin")
                        )
                ),
                Aggregates.facet(
                        new Facet("results",
                                Aggregates.sort(Sorts.ascending(DATE_FIELD)),
                                Aggregates.skip(page * size),
                                Aggregates.limit(size)
                        ),
                        new Facet("totalCount",
                                Aggregates.count("count")
                        )
                )
        );


        return Multi.createFrom().publisher(collection.aggregate(pipeline));
    }
}
