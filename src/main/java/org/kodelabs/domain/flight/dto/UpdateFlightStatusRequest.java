package org.kodelabs.domain.flight.dto;

import jakarta.validation.constraints.NotNull;
import org.kodelabs.domain.flight.enums.FlightStatus;
import org.kodelabs.domain.flight.validation.ValidFlightStatusUpdate;

import java.time.Instant;

@ValidFlightStatusUpdate
public class UpdateFlightStatusRequest {

    @NotNull
    private FlightStatus status;

    private Instant newPlannedDepartureTime;
    private Instant newPlannedArrivalTime;

    private Instant actualDepartureTime;
    private Instant actualArrivalTime;

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public Instant getNewPlannedDepartureTime() {
        return newPlannedDepartureTime;
    }

    public void setNewPlannedDepartureTime(Instant newPlannedDepartureTime) {
        this.newPlannedDepartureTime = newPlannedDepartureTime;
    }

    public Instant getNewPlannedArrivalTime() {
        return newPlannedArrivalTime;
    }

    public void setNewPlannedArrivalTime(Instant newPlannedArrivalTime) {
        this.newPlannedArrivalTime = newPlannedArrivalTime;
    }

    public Instant getActualArrivalTime() {
        return actualArrivalTime;
    }

    public void setActualArrivalTime(Instant actualArrivalTime) {
        this.actualArrivalTime = actualArrivalTime;
    }

    public Instant getActualDepartureTime() {
        return actualDepartureTime;
    }

    public void setActualDepartureTime(Instant actualDepartureTime) {
        this.actualDepartureTime = actualDepartureTime;
    }
}
