package org.kodelabs.domain.airport.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.airport.repository.AirportRepository;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.airport.mapper.AirportMapper;
import org.kodelabs.domain.common.PaginatedResponse;
import org.kodelabs.domain.common.pagination.PaginationMapper;

@ApplicationScoped
public class AirportService {

    @Inject
    AirportRepository repository;

    public Uni<AirportDTO> findOneByIata(String iata) {
        return repository.findOneByIata(iata).onItem().transform(AirportMapper::mapToDTO);
    }

    public Uni<PaginatedResponse<AirportDTO>> findAll(int page, int size, boolean ascending) {
        return repository.findAirportsWithPagination(page, size, ascending)
                .map(result -> PaginationMapper.toPaginatedResponse(result, page, size, AirportMapper::mapToDTO));
    }
}

