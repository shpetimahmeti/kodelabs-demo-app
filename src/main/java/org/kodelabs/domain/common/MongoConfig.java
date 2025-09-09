package org.kodelabs.domain.common;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.entity.FlightEntity;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

@ApplicationScoped
public class MongoConfig {

    @Inject
    ReactiveMongoClient client;

    @Inject
    MongoClient nonReactiveClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.collections.airport")
    String airportCollection;

    @ConfigProperty(name = "app.mongodb.collections.flight")
    String flightCollection;

    @ConfigProperty(name = "app.mongodb.collections.reservation")
    String reservationCollection;

    //AIRPORT
    @Produces
    @ApplicationScoped
    public ReactiveMongoCollection<AirportEntity> airportCollection() {
        return getCollection(airportCollection, AirportEntity.class);
    }

    //FLIGHT
    @Produces
    @ApplicationScoped
    public ReactiveMongoCollection<FlightWithConnections> flightWithConnectionsCollection() {
        return getCollection(flightCollection, FlightWithConnections.class);
    }

    @Produces
    @ApplicationScoped
    public ReactiveMongoCollection<FlightEntity> flightCollection() {
        return getCollection(flightCollection, FlightEntity.class);
    }

    //RESERVATION
    @Produces
    @ApplicationScoped
    public ReactiveMongoCollection<ReservationEntity> reservationCollection() {
        return getCollection(reservationCollection, ReservationEntity.class);
    }

    //NON REACTIVE --------------------------------------------
    @Produces
    @ApplicationScoped
    public MongoCollection<FlightEntity> nonReactiveFlightCollection() {
        return getNonReactiveCollection(flightCollection, FlightEntity.class);
    }

    @Produces
    @ApplicationScoped
    public MongoCollection<AirportEntity> nonReactiveAirportCollection() {
        return getNonReactiveCollection(airportCollection, AirportEntity.class);
    }

    private <T> ReactiveMongoCollection<T> getCollection(String collectionName, Class<T> entityClass) {
        return client.getDatabase(database).getCollection(collectionName, entityClass);
    }

    private <T> MongoCollection<T> getNonReactiveCollection(String collectionName,
                                                           Class<T> entityClass) {
        return nonReactiveClient.getDatabase(database)
                .getCollection(collectionName, entityClass);
    }
}
