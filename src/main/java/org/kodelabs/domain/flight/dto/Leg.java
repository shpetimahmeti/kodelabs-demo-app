package org.kodelabs.domain.flight.dto;

import org.kodelabs.domain.flight.FlightEntity;

import java.time.Instant;
import java.util.List;

public class Leg {
    private int legIndex;
    private String from;
    private String to;
    private Instant departureTime;
    private Instant arrivalTime;
    private String aircraftRegistration;
    private String aircraftType;
    private String status;
    private List<Seat> seatMap;
    private int availableSeatsCount;

    public int getLegIndex() { return legIndex; }
    public void setLegIndex(int legIndex) { this.legIndex = legIndex; }

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }

    public Instant getDepartureTime() { return departureTime; }
    public void setDepartureTime(Instant departureTime) { this.departureTime = departureTime; }

    public Instant getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(Instant arrivalTime) { this.arrivalTime = arrivalTime; }

    public String getAircraftRegistration() { return aircraftRegistration; }
    public void setAircraftRegistration(String aircraftRegistration) { this.aircraftRegistration = aircraftRegistration; }

    public String getAircraftType() { return aircraftType; }
    public void setAircraftType(String aircraftType) { this.aircraftType = aircraftType; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<Seat> getSeatMap() { return seatMap; }
    public void setSeatMap(List<Seat> seatMap) { this.seatMap = seatMap; }

    public int getAvailableSeatsCount() { return availableSeatsCount; }
    public void setAvailableSeatsCount(int availableSeatsCount) { this.availableSeatsCount = availableSeatsCount; }
}