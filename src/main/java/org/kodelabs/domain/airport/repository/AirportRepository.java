package org.kodelabs.domain.airport.repository;

import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Facet;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.bson.conversions.Bson;
import org.kodelabs.domain.airport.dto.AirportFacetResult;
import org.kodelabs.domain.airport.entity.AirportEntity;

import java.util.List;

@ApplicationScoped
public class AirportRepository {

    private final String NAME_FIELD = "name";

    @Inject
    ReactiveMongoCollection<AirportEntity> airportCollection;

    @Inject
    ReactiveMongoCollection<AirportFacetResult> airportPaginationCollection;

    public Uni<AirportEntity> findOneByIata(String iata) {
        return Multi.createFrom().publisher(airportCollection.find(Filters.eq("iata", iata)))
                .collect().first()
                .onItem().ifNull().failWith(() -> new RuntimeException("Not found"));
    }

    public Multi<AirportFacetResult> findAirportsWithPagination(int page, int size, boolean ascending) {
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

        return Multi.createFrom().publisher(airportPaginationCollection.aggregate(pipeline));
    }
}
