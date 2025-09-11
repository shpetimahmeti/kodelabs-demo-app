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
        } catch (Exception e) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage())
                    .addConstraintViolation();
            return false;
        }
    }

    public static void validateTimesValuesPresenceForDelayedFlights(UpdateFlightStatusRequest request) {
        if (request.getStatus() == FlightStatus.DELAYED) {
            if (request.getNewDepartureTime() == null && request.getNewArrivalTime() == null) {
                throw new IllegalArgumentException("Delayed flights must have updated times");
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
