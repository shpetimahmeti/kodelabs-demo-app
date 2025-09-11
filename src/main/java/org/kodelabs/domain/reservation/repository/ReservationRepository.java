package org.kodelabs.domain.reservation.repository;

import com.mongodb.client.model.Filters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

import static org.kodelabs.domain.common.util.Fields.ID;
import static org.kodelabs.domain.common.util.Fields.ReservationFields.USER_ID;

@ApplicationScoped
public class ReservationRepository extends BaseRepository<ReservationEntity> {

    @Inject
    public ReservationRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, ReservationEntity.class);
    }

    public Uni<ReservationEntity> findByObjectId(String id) {
        return find(Filters.eq(ID, id)).collect().first();
    }

    public Uni<PaginationFacetResult<ReservationEntity>> findByUserId(String userId, int page, int size,String sortField, boolean ascending) {
        return loadPaginationFacetResult(
                Filters.eq(USER_ID, userId),
                page,
                size,
                sortField,
                ascending);
    }
}
