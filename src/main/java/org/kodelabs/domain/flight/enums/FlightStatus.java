package org.kodelabs.domain.flight.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
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

    @JsonCreator
    public static FlightStatus fromValue(String value) {
        for (FlightStatus status : values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }

        throw new IllegalArgumentException("Invalid flight status: " + value);
    }
}
