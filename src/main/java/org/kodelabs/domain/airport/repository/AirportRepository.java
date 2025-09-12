package org.kodelabs.domain.airport.repository;

import com.mongodb.client.model.Filters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.common.mongo.MongoRegistry;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;

import static org.kodelabs.domain.common.mongo.Fields.AirportFields.IATA;

@ApplicationScoped
public class AirportRepository extends BaseRepository<AirportEntity> {

    @Inject
    public AirportRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, AirportEntity.class);
    }

    public Uni<AirportEntity> findOneByIata(String iata) {
        return find(Filters.eq(IATA, iata)).collect().first();
    }

    public Uni<PaginationFacetResult<AirportEntity>> findAirportsWithPagination(int page, int size, String sortField, boolean ascending) {
        return loadPaginationFacetResult(
                page, size,
                sortField,
                ascending);
    }
}