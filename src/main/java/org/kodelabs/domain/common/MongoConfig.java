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
import org.kodelabs.domain.reservation.entity.ReservationEntity;

import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class MongoConfig {

    private final Map<Class<?>, ReactiveMongoCollection<?>> collections = new HashMap<>();
    private final Map<Class<?>, MongoCollection<?>> nonReactiveCollections = new HashMap<>();

    ReactiveMongoClient client;
    MongoClient nonReactiveClient;

    @Inject
    public MongoConfig(ReactiveMongoClient client,
                       MongoClient nonReactiveClient,
                       @ConfigProperty(name = "quarkus.mongodb.database") String databaseName,
                       @ConfigProperty(name = "app.mongodb.collections.airport") String airportCollectionName,
                       @ConfigProperty(name = "app.mongodb.collections.flight") String flightCollectionName,
                       @ConfigProperty(name = "app.mongodb.collections.reservation") String reservationCollectionName) {

        this.client = client;
        this.nonReactiveClient = nonReactiveClient;

        collections.put(AirportEntity.class, client.getDatabase(databaseName).getCollection(airportCollectionName, AirportEntity.class));
        collections.put(FlightEntity.class, client.getDatabase(databaseName).getCollection(flightCollectionName, FlightEntity.class));
        collections.put(ReservationEntity.class, client.getDatabase(databaseName).getCollection(reservationCollectionName, ReservationEntity.class));

        nonReactiveCollections.put(AirportEntity.class, nonReactiveClient.getDatabase(databaseName).getCollection(airportCollectionName, AirportEntity.class));
        nonReactiveCollections.put(FlightEntity.class, nonReactiveClient.getDatabase(databaseName).getCollection(flightCollectionName, FlightEntity.class));
    }

    @SuppressWarnings("unchecked")
    public <T> ReactiveMongoCollection<T> getCollection(Class<T> entityClass) {
        return (ReactiveMongoCollection<T>) collections.get(entityClass);
    }

    @SuppressWarnings("unchecked")
    public <T> MongoCollection<T> getNonReactiveCollection(Class<T> entityClass) {
        return (MongoCollection<T>) nonReactiveCollections.get(entityClass);
    }
}
