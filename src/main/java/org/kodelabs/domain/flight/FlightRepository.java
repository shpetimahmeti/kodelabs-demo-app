package org.kodelabs.domain.flight;

import com.mongodb.client.model.Filters;
import io.quarkus.mongodb.reactive.ReactiveMongoClient;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.kodelabs.domain.flight.entity.FlightEntity;

@ApplicationScoped
public class FlightRepository {

    @Inject
    ReactiveMongoClient client;

    @ConfigProperty(name = "quarkus.mongodb.database")
    String database;

    @ConfigProperty(name = "app.mongodb.flight-collection")
    String flightCollection;

    public Uni<FlightEntity> findOneByObjectId(String objectId) {
        ReactiveMongoCollection<FlightEntity> collection = getCollection();

        return Multi.createFrom().publisher(collection.find(Filters.eq("_id", new ObjectId(objectId))))
                .collect().first()
                .onItem().ifNull().failWith(() -> new RuntimeException("Not found"));
        //TODO update exception type, and implement exception handler
    }

    private ReactiveMongoCollection<FlightEntity> getCollection() {
        return client.getDatabase(database).getCollection(flightCollection, FlightEntity.class);
    }
}
