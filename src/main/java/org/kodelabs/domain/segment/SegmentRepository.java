package org.kodelabs.domain.segment;

import com.mongodb.client.model.*;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@ApplicationScoped
public class SegmentRepository {

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.segment-collection")
    String segmentCollection;

    private final String SEGMENTS_FIELD = "segments";
    private final String FROM_FIELD = "from";
    private final String TO_FIELD = "to";
    private final String DATE_FIELD = "date";

    public Uni<SegmentEntity> getFirstSegment() {
        ReactiveMongoCollection<SegmentEntity> collection = getCollection();

        return Uni.createFrom().publisher(collection.find());
    }

    private ReactiveMongoCollection<SegmentEntity> getCollection() {
        return client.getDatabase(database)
                .getCollection(segmentCollection, SegmentEntity.class);
    }

    public Multi<SegmentEntity> findSegments(String originIata,
                                             String destination,
                                             LocalDate startDepartureDate,
                                             LocalDate endDepartureDate) {
        ReactiveMongoCollection<SegmentEntity> collection = getCollection();

        GraphLookupOptions options = new GraphLookupOptions();

        options.maxDepth(3);
        options.depthField("depth");
        options.restrictSearchWithMatch(Filters.and(
                Filters.eq("stops", 0),
                Filters.lte("departureTime", Instant.parse("2025-09-07T00:00:00Z"))
        ));

        List<Bson> pipeline = List.of(
                //match
                Aggregates.match(
                        Filters.and(
                                Filters.eq("from", originIata),
                                Filters.eq("stops", 0),
                                Filters.gte("departureTime", startDepartureDate),
                                Filters.lte("departureTime", endDepartureDate)
                        )),

                //sort
                Aggregates.sort(Sorts.ascending("departureTime")),

                //graphLookup
                Aggregates.graphLookup(
                        "segments",
                        "$to",
                        "to",
                        "from",
                        "connections",
                        new GraphLookupOptions()
                ),
                //match end destination
                Aggregates.match(Filters.or(
                        Filters.eq("to", destination),
                        Filters.eq("connections.to", destination)
                )),

                //addFields (sort connections by departureTime)
                Aggregates.addFields(
                        new Field<>("connections",
                        new Document("$sortArray",
                                new Document("input", "$connections")
                                        .append("sortBy", new Document("departureTime", 1))
                        )
                )),
                //skip
                Aggregates.skip(0),
                //limit
                Aggregates.limit(10)
        );

        return collection.aggregate(pipeline);
    }

}
