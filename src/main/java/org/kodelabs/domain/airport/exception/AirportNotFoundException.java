package org.kodelabs.domain.airport.exception;

import org.kodelabs.domain.common.exception.NotFoundException;

public class AirportNotFoundException extends NotFoundException {

    public AirportNotFoundException(String iata) {
        super("Airport not found, IATA code: " + iata);
    }
}
