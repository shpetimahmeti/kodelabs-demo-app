package org.kodelabs.domain.common;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.flight.entity.FlightEntity;

@ApplicationScoped
public class MongoConfig {

    @Inject
    ReactiveMongoClient client;

    @Inject
    MongoClient nonReactiveClient;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.flight-collection")
    String flightCollection;

    @ConfigProperty(name = "app.mongodb.airport-collection")
    String airportCollection;

    public ReactiveMongoCollection<FlightEntity> getFlightCollection() {
        return client.getDatabase(database).getCollection(flightCollection, FlightEntity.class);
    }

    public <T> ReactiveMongoCollection<T> getFlightCollection(Class<T> entityClass) {
        return client.getDatabase(database).getCollection(flightCollection, entityClass);
    }

    public MongoCollection<FlightEntity> getNonReactiveFlightCollection() {
        return getNonReactiveCollection(flightCollection, FlightEntity.class);
    }

    public MongoCollection<AirportEntity> getNonReactiveAirportCollection() {
        return getNonReactiveCollection(airportCollection, AirportEntity.class);
    }

    public <T> ReactiveMongoCollection<T> getCollection(String collectionName,
                                                        Class<T> entityClass) {
        return client.getDatabase(database)
                .getCollection(collectionName, entityClass);
    }

    public <T> MongoCollection<T> getNonReactiveCollection(String collectionName,
                                                           Class<T> entityClass) {
        return nonReactiveClient.getDatabase(database)
                .getCollection(collectionName, entityClass);
    }
}
