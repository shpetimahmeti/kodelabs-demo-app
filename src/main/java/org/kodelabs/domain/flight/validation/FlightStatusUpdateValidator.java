package org.kodelabs.domain.flight.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.kodelabs.domain.flight.dto.UpdateFlightStatusRequest;
import org.kodelabs.domain.flight.enums.FlightStatus;

import java.util.Set;

public class FlightStatusUpdateValidator implements ConstraintValidator<ValidFlightStatusUpdate, UpdateFlightStatusRequest> {

    @Override
    public boolean isValid(UpdateFlightStatusRequest request, ConstraintValidatorContext context) {
        if (request == null) {
            return true;
        }

        try {
            validateTimesValuesPresenceForDelayedFlights(request);
            return true;
        } catch (Exception ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ex.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    public static void validateTimesValuesPresenceForDelayedFlights(UpdateFlightStatusRequest request) {
        if (request.getStatus() == FlightStatus.DELAYED) {
            if (request.getNewPlannedDepartureTime() == null && request.getNewPlannedArrivalTime() == null) {
                throw new IllegalArgumentException("Delayed flights must have updated times");
            }
        }
        if (request.getStatus() == FlightStatus.DEPARTED) {
            if (request.getActualDepartureTime() == null) {
                throw new IllegalArgumentException("Departed flights must have actual departure time set");
            }
        }

        if (request.getStatus() == FlightStatus.LANDED) {
            if (request.getActualArrivalTime() == null) {
                throw new IllegalArgumentException("Landed flights must have actual arrival time set");
            }
        }

    }

    public static Set<FlightStatus> allowedPreviousStatuses(FlightStatus newStatus) {
        return switch (newStatus) {
            case SCHEDULED -> Set.of();
            case BOARDING -> Set.of(FlightStatus.SCHEDULED, FlightStatus.DELAYED);
            case DEPARTED -> Set.of(FlightStatus.BOARDING, FlightStatus.DELAYED);
            case DELAYED -> Set.of(FlightStatus.DELAYED, FlightStatus.SCHEDULED, FlightStatus.BOARDING, FlightStatus.DEPARTED);
            case LANDED -> Set.of(FlightStatus.DEPARTED, FlightStatus.DELAYED);
            case CANCELLED -> Set.of(FlightStatus.SCHEDULED, FlightStatus.BOARDING, FlightStatus.DELAYED);
        };
    }
}
