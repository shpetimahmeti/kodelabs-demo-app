package org.kodelabs.domain.flight.exception;

public class InvalidFlightStatusTransitionException extends RuntimeException {

    private final String toStatus;

    public InvalidFlightStatusTransitionException(String toStatus) {
        super("Invalid transition to " + toStatus);
        this.toStatus = toStatus;
    }

    public String getToStatus() {
        return toStatus;
    }

}
