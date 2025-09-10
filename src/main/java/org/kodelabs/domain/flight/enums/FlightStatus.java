package org.kodelabs.domain.flight.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum FlightStatus {
    SCHEDULED,
    BOARDING,
    DEPARTED,
    DELAYED,
    LANDED,
    CANCELLED;

    @JsonValue
    public String toValue() {
        return name().toLowerCase();
    }
}
