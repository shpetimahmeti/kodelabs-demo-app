package org.kodelabs.domain.flight.repository;

import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.kodelabs.domain.common.MongoConfig;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.entity.FlightEntity;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class FlightRepository extends BaseRepository<FlightEntity> {

    //TODO move to mongo config
    private final String FROM_IATA = "from.iata";
    private final String TO_IATA = "to.iata";
    private final String TO_IATA_START_WITH_VALUE = "$" + TO_IATA;
    private final String DEPARTURE_TIME = "departureTime";
    private final String FLIGHT_COLLECTION_NAME = "flights";

    private final String CONNECTIONS_FIELD = "connections";
    private final String CONNECTION_VALUE = "$" + CONNECTIONS_FIELD;
    private final String CONNECTIONS_TO_IATA = "connections.to.iata";

    @Inject
    public FlightRepository(MongoConfig mongoConfig) {
        super(mongoConfig, FlightEntity.class);
    }

    public Uni<UpdateResult> reserveSeat(ClientSession session, String flightId, String seatNumber) {
        Document filter = new Document("_id", flightId)
                .append("seats", new Document("$elemMatch",
                        new Document("seatNumber", seatNumber).append("available", true)));

        Document update = new Document("$set", new Document("seats.$.available", false))
                .append("$inc", new Document("availableSeatsCount", -1));

        return collection.updateOne(session, filter, update);
    }

    public Uni<FlightEntity> findOneByObjectId(String id) {
        //TODO update exception type, and implement exception handler
        return Multi.createFrom().publisher(collection.find(Filters.eq("_id", id)))
                .collect().first()
                .onItem().ifNull().failWith(() -> new RuntimeException("Not found"));
    }

    public Multi<FlightWithConnections> findConnectionsFromOriginToDestination(String originIata,
                                                                               String destIata,
                                                                               LocalDate departureDateMin,
                                                                               LocalDate departureDateMax) {
        List<Bson> pipeline = buildThePipeline(
                originIata,
                destIata,
                departureDateMin,
                departureDateMax);

        return collection.withDocumentClass(FlightWithConnections.class).aggregate(pipeline);
    }

    private List<Bson> buildThePipeline(String originIata,
                                        String destIata,
                                        LocalDate departureDateMin,
                                        LocalDate departureDateMax) {

        GraphLookupOptions options = buildOptions(departureDateMax);

        return List.of(
                Aggregates.match(
                        Filters.and(
                                Filters.eq(FROM_IATA, originIata),
                                Filters.gte(DEPARTURE_TIME, departureDateMin),
                                Filters.lte(DEPARTURE_TIME, departureDateMax)
                        )),
                Aggregates.sort(Sorts.ascending(DEPARTURE_TIME)),
                Aggregates.graphLookup(
                        FLIGHT_COLLECTION_NAME,
                        TO_IATA_START_WITH_VALUE,
                        TO_IATA,
                        FROM_IATA,
                        CONNECTIONS_FIELD,
                        options
                ),
                Aggregates.match(Filters.or(
                        Filters.eq(TO_IATA, destIata),
                        Filters.eq(CONNECTIONS_TO_IATA, destIata)
                )),
                Aggregates.addFields(
                        new Field<>(CONNECTIONS_FIELD,
                                new Document("$sortArray",
                                        new Document("input", CONNECTION_VALUE)
                                                .append("sortBy", Sorts.ascending(DEPARTURE_TIME))
                                )
                        ))
        );
    }

    private GraphLookupOptions buildOptions(LocalDate departureDateMax) {
        GraphLookupOptions options = new GraphLookupOptions();

        options.maxDepth(3);
        options.depthField("depth");
        options.restrictSearchWithMatch(Filters.and(
                Filters.lte("departureTime", departureDateMax)
        ));

        return options;
    }
}
