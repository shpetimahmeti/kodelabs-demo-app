package org.kodelabs.domain.startup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.MongoConfig;
import org.kodelabs.domain.flight.entity.FlightEntity;

import java.io.InputStream;
import java.time.*;
import java.util.List;

@ApplicationScoped
public class FlightGeneratorService {

    @Inject
    MongoConfig mongoConfig;

    @Inject
    ObjectMapper objectMapper;

    public void onStart(@Observes StartupEvent ev) {
        MongoCollection<FlightEntity> collection = mongoConfig.getNonReactiveFlightCollection();

        try (InputStream is = getClass().getResourceAsStream("/flights.json")) {
            if (is == null) {
                throw new IllegalStateException("Flights.json not found");
            }

            List<FlightEntity> flightEntities = objectMapper
                    .readerFor(new TypeReference<List<FlightEntity>>() {})
                    .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                    .readValue(is);


            for (FlightEntity flight : flightEntities) {
                flight.setCreatedAt(Instant.now());
                flight.setUpdatedAt(Instant.now());

                if (collection.find(new org.bson.Document("_id", flight.getId())).first() == null) {
                    collection.insertOne(flight);
                    System.out.println("Inserted flight " + flight.getId());
                } else {
                    System.out.println("Skipping duplicate " + flight.getId());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load flights.json", e);
        }
    }
}
