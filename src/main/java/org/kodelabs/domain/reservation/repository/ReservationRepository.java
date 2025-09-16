package org.kodelabs.domain.reservation.repository;

import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.pagination.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;
import org.kodelabs.domain.reservation.dto.ReservationsPerDayResponse;
import org.kodelabs.domain.reservation.dto.TopUserReservationsResponse;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.kodelabs.domain.common.mongo.Fields.AggregationFields.COUNT_FIELD;
import static org.kodelabs.domain.common.mongo.Fields.CREATED_AT;
import static org.kodelabs.domain.common.mongo.Fields.ID;
import static org.kodelabs.domain.common.mongo.Fields.ReservationFields.USER_ID;
import static org.kodelabs.domain.common.mongo.Fields.asFieldRef;

@ApplicationScoped
public class ReservationRepository extends BaseRepository<ReservationEntity> {


    @Inject
    public ReservationRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, ReservationEntity.class);
    }

    public Uni<ReservationEntity> findByObjectId(String id) {
        return find(Filters.eq(ID, id)).collect().first();
    }

    public Uni<PaginationFacetResult<ReservationEntity>> findByUserId(String userId, int page, int size, String sortField, boolean ascending) {
        return loadPaginationFacetResult(
                Filters.eq(USER_ID, userId),
                page,
                size,
                sortField,
                ascending);
    }

    public Uni<List<TopUserReservationsResponse>> findTopUsers(int limit) {
        List<Bson> pipeline = List.of(
                Aggregates.group(asFieldRef(USER_ID), Accumulators.sum(COUNT_FIELD, 1)),
                Aggregates.sort(Sorts.descending(COUNT_FIELD)),
                Aggregates.limit(limit)
        );

        return withDocumentClass(Document.class, pipeline)
                .map(doc -> new TopUserReservationsResponse(
                        doc.getString(ID),
                        doc.getInteger(COUNT_FIELD, 0)
                ))
                .collect().asList();
    }

    public Uni<List<ReservationsPerDayResponse>> countReservationsPerDay(LocalDate from, LocalDate to) {
        List<Bson> filters = new ArrayList<>();

        filters.add(Filters.gte(CREATED_AT, Date.from(from.atStartOfDay(ZoneOffset.UTC).toInstant())));
        filters.add(Filters.lt(CREATED_AT, Date.from(to.atStartOfDay(ZoneOffset.UTC).toInstant())));

        List<Bson> pipeline = new ArrayList<>();
        pipeline.add(Aggregates.match(Filters.and(filters)));
        pipeline.add(Aggregates.group(
                new Document("$dateToString", new Document("format", "%Y-%m-%d").append("date", asFieldRef(CREATED_AT))),
                Accumulators.sum(COUNT_FIELD, 1)
        ));
        pipeline.add(Aggregates.sort(Sorts.ascending(ID)));

        System.out.println(filters);

        return withDocumentClass(Document.class, pipeline).map(doc ->
                        new ReservationsPerDayResponse(
                                LocalDate.parse(doc.getString(ID)),
                                doc.getInteger(COUNT_FIELD).longValue())
                )
                .collect().asList();
    }
}
