package org.kodelabs.domain.flight;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.bson.types.ObjectId;
import org.kodelabs.domain.common.PaginatedResponse;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.dto.FlightFacetResult;

import java.util.List;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightRepository repository;

    public Uni<PaginatedResponse<FlightDTO>> findAll(String originIata, String destinationIata, int page, int size) {
        return repository.findFlight(originIata, destinationIata, page, size).collect().asList().map(list -> {
            if (list.isEmpty()) {
                return new PaginatedResponse<>(List.of(), page, size, 0L);
            }

            FlightFacetResult facet = list.getFirst();
            long total = facet.getTotalCount().isEmpty() ? 0L : facet.getTotalCount().getFirst().getCount();

            return new PaginatedResponse<>(facet.getResults().stream().map(this::mapToDTO).toList(), page, size, total);
        });
    }

    private FlightDTO mapToDTO(FlightEntity entity) {
        FlightDTO dto = new FlightDTO();

        ObjectId objectId = entity.getId();
        String id = objectId != null ? objectId.toString() : null;

        dto.setId(id);
        dto.setRouteId(entity.getRouteId());
        dto.setRouteVersion(entity.getRouteVersion());
        dto.setFlightNumber(entity.getFlightNumber());
        dto.setDate(entity.getDate());
        dto.setOriginIata(entity.getOriginIata());
        dto.setDestinationIata(entity.getDestinationIata());
        dto.setOriginTimezone(entity.getOriginTimezone());
        dto.setDestinationTimezone(entity.getDestinationTimezone());
        dto.setOverallStatus(entity.getOverallStatus());
        dto.setLegs(entity.getLegs());
        dto.setTotalAvailableSeats(entity.getTotalAvailableSeats());

        return dto;
    }
}
