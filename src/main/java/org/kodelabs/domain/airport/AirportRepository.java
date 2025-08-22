package org.kodelabs.domain.airport;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.airport.dto.AirportFacetResult;

import java.util.List;

@ApplicationScoped
public class AirportRepository {

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.airport-collection")
    String airportCollection;

    private final String NAME_FIELD = "name";

    public Multi<AirportFacetResult> findAirportsWithPagination(int page, int size, boolean ascending) {
        ReactiveMongoCollection<AirportFacetResult> collection = client.getDatabase(database)
                .getCollection(airportCollection, AirportFacetResult.class);


        List<Bson> pipeline = List.of(
                Aggregates.facet(
                        new Facet("results",
                                Aggregates.sort(ascending ? Sorts.ascending(NAME_FIELD) : Sorts.descending(NAME_FIELD)),
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
