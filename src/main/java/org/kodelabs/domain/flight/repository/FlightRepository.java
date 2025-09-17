package org.kodelabs.domain.flight.repository;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Field;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.GraphLookupOptions;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.kodelabs.domain.common.mongo.util.AggregationExprs;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.flight.dto.AverageDelayResponse;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.entity.FlightEntity;
import org.kodelabs.domain.flight.enums.FlightStatus;
import org.kodelabs.domain.flight.validation.FlightStatusUpdateValidator;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.inc;
import static com.mongodb.client.model.Updates.set;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.ACTUAL_ARRIVAL_TIME;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.ACTUAL_DEPARTURE_TIME;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.ARRIVAL_TIME;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.AVAILABLE_SEATS_COUNT;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.CONNECTIONS;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.CONNECTIONS__TO__IATA;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.DELAYED_COUNT;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.DEPARTURE_TIME;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.FLIGHT_COLLECTION_NAME;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.FROM__IATA;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.SEATS;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.SEAT_NUMBER;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.STATUS;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.TO__IATA;
import static org.kodelabs.domain.common.mongo.Fields.FlightFields.__AVAILABLE;
import static org.kodelabs.domain.common.mongo.Fields.ID;
import static org.kodelabs.domain.common.mongo.Fields.asFieldRef;
import static org.kodelabs.domain.common.mongo.Fields.positionalField;

@ApplicationScoped
public class FlightRepository extends BaseRepository<FlightEntity> {

    @Inject
    public FlightRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, FlightEntity.class);
    }

    public Uni<UpdateResult> reserveSeat(ClientSession session, String flightId, String seatNumber) {
        Document filter = new Document(ID, flightId).append(SEATS,
                AggregationExprs.elemMatch(new Document(SEAT_NUMBER, seatNumber).append(__AVAILABLE, true)));

        Bson update = combine(
                set(positionalField(SEATS, __AVAILABLE), false),
                inc(AVAILABLE_SEATS_COUNT, -1)
        );

        return updateOne(session, filter, update);
    }

    public Uni<FlightEntity> updateFlightStatus(String flightId,
                                                FlightStatus newStatus,
                                                Instant newArrivalTime,
                                                Instant newDepartureTime,
                                                Instant actualDepartureTime,
                                                Instant actualArrivalTime) {
        Set<FlightStatus> allowedPrevStatuses = FlightStatusUpdateValidator.allowedPreviousStatuses(newStatus);

        Bson filter = Filters.and(
                Filters.eq(ID, flightId),
                Filters.in(STATUS, allowedPrevStatuses.stream().map(FlightStatus::toValue).toList())
        );

        List<Bson> updates = new ArrayList<>();
        updates.add(set(STATUS, newStatus.toValue()));

        if (newStatus == FlightStatus.DELAYED) {
            if (newDepartureTime != null) {
                updates.add(set(DEPARTURE_TIME, newDepartureTime));
            }
            if (newArrivalTime != null) {
                updates.add(set(ARRIVAL_TIME, newArrivalTime));
            }
            updates.add(inc(DELAYED_COUNT, 1));
        }

        if (newStatus == FlightStatus.DEPARTED) {
            updates.add(set(ACTUAL_DEPARTURE_TIME, actualDepartureTime));
        }

        if (newStatus == FlightStatus.LANDED) {
            updates.add(set(ACTUAL_ARRIVAL_TIME, actualArrivalTime));
        }

        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER);
        return findOneAndUpdateWithOptions(filter, Updates.combine(updates), options);
    }

    public Uni<UpdateResult> releaseSeat(ClientSession session, String flightId, String seatNumber) {
        Document filter = new Document(ID, flightId)
                .append(SEATS, AggregationExprs.elemMatch(
                        new Document(SEAT_NUMBER, seatNumber)
                ));

        Bson update = combine(
                set(positionalField(SEATS, __AVAILABLE), true),
                inc(AVAILABLE_SEATS_COUNT, 1)
        );

        return updateOne(session, filter, update);
    }

    public Uni<FlightEntity> findOneByObjectId(String id) {
        return find(Filters.eq(ID, id)).collect().first();
    }

    public Uni<List<AverageDelayResponse>> getAverageDelays(String groupByField) {
        List<Bson> pipeline = new ArrayList<>();

        pipeline.add(Aggregates.match(Filters.and(
                Filters.exists(ACTUAL_DEPARTURE_TIME, true),
                Filters.exists(ACTUAL_ARRIVAL_TIME, true)
        )));

        // 2. Project delays in minutes
        pipeline.add(Aggregates.project(Projections.fields(
                Projections.computed("groupKey", asFieldRef(groupByField)),
                Projections.computed("departureDelay",
                        new Document("$divide", List.of(
                                new Document("$subtract", List.of(asFieldRef(ACTUAL_DEPARTURE_TIME), asFieldRef(DEPARTURE_TIME))),
                                1000 * 60
                        ))),
                Projections.computed("arrivalDelay",
                        new Document("$divide", List.of(
                                new Document("$subtract", List.of(asFieldRef(ACTUAL_ARRIVAL_TIME), asFieldRef(ARRIVAL_TIME))),
                                1000 * 60
                        )))
        )));

        // 3. Group and average
        pipeline.add(Aggregates.group("$groupKey",
                Accumulators.avg("avgDepartureDelay", "$departureDelay"),
                Accumulators.avg("avgArrivalDelay", "$arrivalDelay")
        ));

        // 4. Sort by biggest delays
        pipeline.add(Aggregates.sort(Sorts.descending("avgDepartureDelay")));

        return withDocumentClass(Document.class, pipeline)
                .map(doc -> new AverageDelayResponse(
                        doc.getString(ID),
                        doc.getDouble("avgDepartureDelay"),
                        doc.getDouble("avgArrivalDelay")
                ))
                .collect().asList();
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


        return withDocumentClass(FlightWithConnections.class, pipeline);
    }

    private List<Bson> buildThePipeline(String originIata,
                                        String destIata,
                                        LocalDate departureDateMin,
                                        LocalDate departureDateMax) {

        GraphLookupOptions options = buildOptions(departureDateMax);

        return List.of(
                Aggregates.match(
                        Filters.and(
                                Filters.eq(FROM__IATA, originIata),
                                Filters.gte(DEPARTURE_TIME, departureDateMin),
                                Filters.lte(DEPARTURE_TIME, departureDateMax)
                        )),
                Aggregates.sort(Sorts.ascending(DEPARTURE_TIME)),
                Aggregates.graphLookup(
                        FLIGHT_COLLECTION_NAME,
                        asFieldRef(TO__IATA),
                        TO__IATA,
                        FROM__IATA,
                        CONNECTIONS,
                        options
                ),
                Aggregates.match(Filters.or(
                        Filters.eq(TO__IATA, destIata),
                        Filters.eq(CONNECTIONS__TO__IATA, destIata)
                )),
                Aggregates.addFields(
                        new Field<>(CONNECTIONS,
                                AggregationExprs.sortArray(
                                        CONNECTIONS,
                                        AggregationExprs.combineSorts(Sorts.ascending(DEPARTURE_TIME))
                                )
                        )
                )
        );
    }

    private GraphLookupOptions buildOptions(LocalDate departureDateMax) {
        GraphLookupOptions options = new GraphLookupOptions();

        options.maxDepth(3);
        options.depthField("depth");
        options.restrictSearchWithMatch(Filters.and(
                Filters.lte(DEPARTURE_TIME, departureDateMax)
        ));

        return options;
    }
}
