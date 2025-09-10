package org.kodelabs.domain.flight.validation;

import org.kodelabs.domain.flight.dto.UpdateFlightStatusRequest;
import org.kodelabs.domain.flight.enums.FlightStatus;

import java.util.Map;
import java.util.Set;

public class FlightStatusValidator {
    private static final Map<FlightStatus, Set<FlightStatus>> ALLOWED_TRANSITIONS = Map.of(
            FlightStatus.SCHEDULED, Set.of(FlightStatus.BOARDING, FlightStatus.DELAYED, FlightStatus.CANCELLED),
            FlightStatus.BOARDING,  Set.of(FlightStatus.DEPARTED, FlightStatus.DELAYED, FlightStatus.CANCELLED),
            FlightStatus.DEPARTED,  Set.of(FlightStatus.LANDED, FlightStatus.DELAYED),
            FlightStatus.DELAYED,   Set.of(FlightStatus.SCHEDULED, FlightStatus.BOARDING, FlightStatus.DEPARTED, FlightStatus.CANCELLED),
            FlightStatus.LANDED,    Set.of(),
            FlightStatus.CANCELLED, Set.of()
    );

    public static void validateTransitionAndTimes(
            FlightStatus currentStatus,
            UpdateFlightStatusRequest request
    ) {
        if (!ALLOWED_TRANSITIONS.getOrDefault(currentStatus, Set.of()).contains(request.getStatus())) {
            throw new IllegalArgumentException(
                    String.format("Invalid status transition: %s → %s", currentStatus, request.getStatus())
            );
        }

        if (request.getStatus() == FlightStatus.DELAYED) {
            if (request.getNewDepartureTime() == null && request.getNewArrivalTime() == null) {
                throw new IllegalArgumentException(
                        "For delayed flights, at least one of departure or arrival time must be provided"
                );
            }
        } else {
            if (request.getNewDepartureTime() != null || request.getNewArrivalTime() != null) {
                throw new IllegalArgumentException(
                        "Departure/arrival times can only be updated when status is DELAYED"
                );
            }
        }
    }
}
