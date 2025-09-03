package org.kodelabs.domain.flight.mapper;

import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.dto.FlightRouteResponse;
import org.kodelabs.domain.flight.entity.FlightEntity;

import java.util.ArrayList;
import java.util.List;

public class FlightMapper {

    public static FlightDTO fromEntity(FlightEntity flight) {
        FlightDTO dto = new FlightDTO();

        dto.setId(flight.getId());
        dto.setPublishedFlightNumber(flight.getPublishedFlightNumber());
        dto.setOperatingFlightNumber(flight.getOperatingFlightNumber());
        dto.setFrom(flight.getFrom());
        dto.setTo(flight.getTo());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setAircraftRegistration(flight.getAircraftRegistration());
        dto.setAircraftType(flight.getAircraftType());
        dto.setAircraftType(flight.getAircraftType());
        dto.setStatus(flight.getStatus());
        dto.setAvailableSeatsCount(flight.getAvailableSeatsCount());
        dto.setSeats(flight.getSeats());
        dto.setPrice(flight.getPrice());

        return dto;
    }

    public static FlightRouteResponse buildFlightRouteResponse(String originIata,
                                                               String destIata,
                                                               List<FlightEntity> flights) {
        FlightRouteResponse response = new FlightRouteResponse();

        response.setOriginIata(originIata);
        response.setDestIata(destIata);


        response.setLegs(new ArrayList<>());

        if (flights == null || flights.isEmpty()) {
            return response;
        }

        response.setStops(flights.size() - 1);

        int totalPrice = 0;
        response.setLegs(flights.stream().map(FlightMapper::fromEntity).toList());

        //        for (FlightEntity flight : flights) {
        //            totalPrice += flight.getPrice();
        //        }

        return response;
    }
}
