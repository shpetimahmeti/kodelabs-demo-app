package org.kodelabs.domain.flight.exception;

import org.kodelabs.domain.common.exception.NotFoundException;

public class FlightNotFoundException extends NotFoundException {

    public FlightNotFoundException(String id) {
        super("Flight not found, ID: " + id);
    }
}
