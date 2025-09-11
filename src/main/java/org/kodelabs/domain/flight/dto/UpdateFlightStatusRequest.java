package org.kodelabs.domain.flight.dto;

import jakarta.validation.constraints.NotNull;
import org.kodelabs.domain.flight.enums.FlightStatus;
import org.kodelabs.domain.flight.validation.ValidFlightStatusUpdate;

import java.time.Instant;

@ValidFlightStatusUpdate
public class UpdateFlightStatusRequest {

    @NotNull
    private FlightStatus status;

    private Instant newDepartureTime;
    private Instant newArrivalTime;


    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public Instant getNewDepartureTime() {
        return newDepartureTime;
    }

    public void setNewDepartureTime(Instant newDepartureTime) {
        this.newDepartureTime = newDepartureTime;
    }

    public Instant getNewArrivalTime() {
        return newArrivalTime;
    }

    public void setNewArrivalTime(Instant newArrivalTime) {
        this.newArrivalTime = newArrivalTime;
    }
}
