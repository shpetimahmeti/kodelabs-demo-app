package org.kodelabs.domain.airport;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.airport.dto.AirportFacetResult;
import org.kodelabs.domain.common.PaginatedResponse;

import java.util.List;

@ApplicationScoped
public class AirportService {

    @Inject
    AirportRepository repository;

    public Uni<AirportDTO> findOneByIata(String iata) {
        return repository.findOneByIata(iata).onItem().transform(this::mapToDTO);
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

                    return new PaginatedResponse<>(facet.getResults().stream().map(this::mapToDTO).toList()  , page, size, total);
                });
    }

    private AirportDTO mapToDTO(AirportEntity entity) {
        AirportDTO dto = new AirportDTO();

        ObjectId objectId = entity.getId();
        String id = objectId != null ? objectId.toString() : null;

        dto.setId(id);
        dto.setName(entity.getName());
        dto.setIata(entity.getIata());
        dto.setIcao(entity.getIcao());
        dto.setCity(entity.getCity());
        dto.setState(entity.getState());
        dto.setCountry(entity.getCountry());
        dto.setElevationFt(entity.getElevationFt());
        dto.setTimezone(entity.getTimezone());
        dto.setLoc(entity.getLoc());

        return dto;
    }

}

