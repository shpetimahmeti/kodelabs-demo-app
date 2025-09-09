package org.kodelabs.domain.airport.repository;

import com.mongodb.client.model.Filters;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import jakarta.inject.Inject;
import org.kodelabs.domain.airport.entity.AirportEntity;
import org.kodelabs.domain.common.MongoRegistry;
import org.kodelabs.domain.common.dto.PaginationFacetResult;
import org.kodelabs.domain.common.repository.BaseRepository;

@ApplicationScoped
public class AirportRepository extends BaseRepository<AirportEntity> {

    private final String NAME_FIELD = "name";

    @Inject
    public AirportRepository(MongoRegistry mongoRegistry) {
        super(mongoRegistry, AirportEntity.class);
    }

    public Uni<AirportEntity> findOneByIata(String iata) {
        return Multi.createFrom().publisher(collection.find(Filters.eq("iata", iata)))
                .collect().first()
                .onItem().ifNull().failWith(() -> new RuntimeException("Not found"));
    }

    public Uni<PaginationFacetResult<AirportEntity>> findAirportsWithPagination(int page, int size, boolean ascending) {
        return loadPaginationFacetResult(
                page, size,
                NAME_FIELD,
                ascending);
    }
}