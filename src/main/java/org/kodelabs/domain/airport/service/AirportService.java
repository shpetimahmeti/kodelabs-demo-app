package org.kodelabs.domain.airport.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.airport.exception.AirportNotFoundException;
import org.kodelabs.domain.airport.repository.AirportRepository;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.airport.mapper.AirportMapper;
import org.kodelabs.domain.common.pagination.dto.PaginatedResponse;
import org.kodelabs.domain.common.pagination.dto.PaginationQueryParams;
import org.kodelabs.domain.common.pagination.mapper.PaginationMapper;

@ApplicationScoped
public class AirportService {

    @Inject
    AirportRepository repository;

    public Uni<AirportDTO> findOneByIata(String iata) {
        return repository.findOneByIata(iata)
                .onItem().ifNull().failWith(() -> new AirportNotFoundException(iata))
                .onItem().transform(AirportMapper::toDto);
    }

    public Uni<PaginatedResponse<AirportDTO>> findAll(PaginationQueryParams params) {
        return repository.findAirportsWithPagination(params.page, params.size, params.sortField, params.ascending)
                .map(result -> PaginationMapper.toPaginatedResponse(result, params.page, params.size, AirportMapper::toDto));
    }
}

