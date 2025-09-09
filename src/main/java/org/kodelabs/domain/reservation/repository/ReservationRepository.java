package org.kodelabs.domain.reservation.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.quarkus.mongodb.reactive.ReactiveMongoCollection;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

import static org.kodelabs.domain.reservation.db.ReservationFields.ID;
import static org.kodelabs.domain.reservation.db.ReservationFields.USER_ID;

@ApplicationScoped
public class ReservationRepository extends BaseRepository<ReservationEntity> {

    ReactiveMongoCollection<ReservationEntity> reservationCollection;

    @Inject
    public ReservationRepository(ReactiveMongoCollection<ReservationEntity> reservationCollection) {
        super(reservationCollection);
        this.reservationCollection = reservationCollection;
    }

    public Uni<InsertOneResult> insertReservation(ClientSession session, ReservationEntity entity) {
        return reservationCollection.insertOne(session, entity);
    }

    public Uni<ReservationEntity> findByObjectId(String id) {
        return reservationCollection.find(Filters.eq(ID, id)).collect().first();
    }

    public Uni<PaginationFacetResult<ReservationEntity>> findByUserId(String userId, int page, int size, boolean ascending) {
        return loadPaginationFacetResult(
                Filters.eq(USER_ID, userId),
                page,
                size,
                ID,
                ascending);
    }
}
