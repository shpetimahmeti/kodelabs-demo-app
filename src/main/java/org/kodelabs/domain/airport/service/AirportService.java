package org.kodelabs.domain.airport.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.airport.repository.AirportRepository;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.airport.dto.AirportFacetResult;
import org.kodelabs.domain.airport.mapper.AirportMapper;
import org.kodelabs.domain.common.PaginatedResponse;

import java.util.List;

@ApplicationScoped
public class AirportService {

    @Inject
    AirportRepository repository;

    public Uni<AirportDTO> findOneByIata(String iata) {
        return repository.findOneByIata(iata).onItem().transform(AirportMapper::mapToDTO);
    }

    public Uni<PaginatedResponse<AirportDTO>> findAll(int page, int size, boolean ascending) {
        return repository.findAirportsWithPagination(page, size, ascending)
                .collect().asList()
                .map(list -> {
                    if (list.isEmpty()) {
                        return new PaginatedResponse<>(List.of(), page, size, 0L);
                    }

                    AirportFacetResult facet = list.getFirst();
                    long total = facet.getTotalCount().isEmpty()
                            ? 0L
                            : facet.getTotalCount().getFirst().getCount();

                    return new PaginatedResponse<>(facet.getResults().stream().map(AirportMapper::mapToDTO).toList(), page, size, total);
                });
    }
}

