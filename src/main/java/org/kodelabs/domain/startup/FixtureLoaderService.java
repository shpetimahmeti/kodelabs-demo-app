package org.kodelabs.domain.startup;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.MongoCollection;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.flight.entity.FlightEntity;

import java.io.IOException;
import java.io.InputStream;
import java.time.*;
import java.util.List;

@ApplicationScoped
public class FixtureLoaderService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    MongoCollection<FlightEntity> flightCollection;

    @Inject
    MongoCollection<AirportEntity> airportCollection;

    public void onStart(@Observes StartupEvent ev) {
        try (InputStream flightIs = getClass().getResourceAsStream("/fixtures/flights.json");
             InputStream airportsIs = getClass().getResourceAsStream("/fixtures/airports.json");
        ) {
            populateFlights(flightIs);
            populateAirportCollection(airportsIs);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load json file", e);
        }
    }

    private void populateFlights(InputStream flightIs) throws IOException {
        if (flightIs == null) {
            throw new IllegalStateException("Flights.json not found");
        }

        List<FlightEntity> flightEntities = objectMapper
                .readerFor(new TypeReference<List<FlightEntity>>() {
                })
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(flightIs);


        for (FlightEntity flight : flightEntities) {
            flight.setCreatedAt(Instant.now());
            flight.setUpdatedAt(Instant.now());

            if (flightCollection.find(new org.bson.Document("_id", flight.get_id())).first() == null) {
                flightCollection.insertOne(flight);
                System.out.println("Inserted flight " + flight.get_id());
            } else {
                System.out.println("Skipping duplicate flight " + flight.get_id());
            }
        }
    }

    private void populateAirportCollection(InputStream airportsIs) throws IOException {
        if (airportsIs == null) {
            throw new IllegalStateException("Airport.json not found");
        }

        List<AirportEntity> airportEntities = objectMapper
                .readerFor(new TypeReference<List<AirportEntity>>() {
                })
                .without(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                .readValue(airportsIs);


        for (AirportEntity airport : airportEntities) {
            airport.createdAt = Instant.now();
            airport.updatedAt = Instant.now();

            if (airportCollection.find(new org.bson.Document("_id", airport.get_id())).first() == null) {
                airportCollection.insertOne(airport);
                System.out.println("Inserted airport " + airport.get_id());
            } else {
                System.out.println("Skipping duplicate airport " + airport.get_id());
            }
        }
    }
}
