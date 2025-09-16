package org.kodelabs.domain.flight.dto;

import java.util.List;

public record FlightAvailabilityResponse(String flightId, List<SeatAvailability> seatClasses) {
    public record SeatAvailability(
            String seatClass,
            int totalSeats,
            int bookedSeats,
            int availableSeats
    ) {}
}
