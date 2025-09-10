package org.kodelabs.domain.airport.repository;

import com.mongodb.client.model.Filters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.common.MongoRegistry;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;

import static org.kodelabs.domain.common.Fields.AirportFields.IATA;
import static org.kodelabs.domain.common.Fields.AirportFields.NAME;

@ApplicationScoped
public class AirportRepository extends BaseRepository<AirportEntity> {

    @Inject
    public AirportRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, AirportEntity.class);
    }

    public Uni<AirportEntity> findOneByIata(String iata) {
        return collection.find(Filters.eq(IATA, iata)).collect().first();
    }

    public Uni<PaginationFacetResult<AirportEntity>> findAirportsWithPagination(int page, int size, boolean ascending) {
        return loadPaginationFacetResult(
                page, size,
                NAME,
                ascending);
    }
}