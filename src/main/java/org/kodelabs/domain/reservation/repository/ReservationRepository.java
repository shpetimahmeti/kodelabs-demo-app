package org.kodelabs.domain.reservation.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

@ApplicationScoped
public class ReservationRepository {

    @Inject
    ReactiveMongoCollection<ReservationEntity> reservationCollection;

    public Uni<InsertOneResult> insertReservation(ClientSession session, ReservationEntity entity) {
        return reservationCollection.insertOne(session, entity);
    }

    public Uni<ReservationEntity> findByObjectId(String id) {
        return reservationCollection.find(Filters.eq("_id", id)).collect().first();
    }
}
