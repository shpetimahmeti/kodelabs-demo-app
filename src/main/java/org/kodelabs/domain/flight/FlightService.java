package org.kodelabs.domain.flight;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.entity.FlightEntity;

@ApplicationScoped
public class FlightService {

    @Inject
    FlightRepository repository;

    public Uni<FlightDTO> findOneByObjectId(String objectId) {
        return repository.findOneByObjectId(objectId).onItem().transform(this::mapToDTO);
    }

    private FlightDTO mapToDTO(FlightEntity entity) {
        FlightDTO dto = new FlightDTO();

        dto.setId(entity.getId().toString());
        dto.setRouteId(entity.getRouteId());
        dto.setRouteVersion(entity.getRouteVersion());
        dto.setOperatingFlightNumber(entity.getOperatingFlightNumber());
        dto.setPublishedFlightNumber(entity.getPublishedFlightNumber());
        dto.setFrom(entity.getFrom());
        dto.setTo(entity.getTo());
        dto.setArrivalTime(entity.getArrivalTime());
        dto.setDepartureTime(entity.getDepartureTime());
        dto.setAircraftType(entity.getAircraftType());
        dto.setAircraftRegistration(entity.getAircraftRegistration());
        dto.setAvailableSeatsCount(entity.getAvailableSeatsCount());
        dto.setRouteInstanceId(entity.getRouteInstanceId());

        return dto;
    }
}
