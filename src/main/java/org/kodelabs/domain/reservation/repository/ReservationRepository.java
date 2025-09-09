package org.kodelabs.domain.reservation.repository;

import com.mongodb.client.model.Filters;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.ClientSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.MongoRegistry;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

import static org.kodelabs.domain.common.Fields.ID;
import static org.kodelabs.domain.common.Fields.ReservationFields.USER_ID;

@ApplicationScoped
public class ReservationRepository extends BaseRepository<ReservationEntity> {

    @Inject
    public ReservationRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, ReservationEntity.class);
    }

    public Uni<InsertOneResult> insertReservation(ClientSession session, ReservationEntity entity) {
        return collection.insertOne(session, entity);
    }

    public Uni<ReservationEntity> findByObjectId(String id) {
        return collection.find(Filters.eq(ID, id)).collect().first();
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
