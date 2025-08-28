package org.kodelabs.domain.route.dto;

import java.time.Instant;
import java.util.List;

public class SegmentDTO {
    private String from;
    private String to;

    private Instant departureTime;
    private Instant arrivalTime;
    private int stops;

    private List<String> stopAirports;
    private List<String> flightIds;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Instant getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(Instant departureTime) {
        this.departureTime = departureTime;
    }

    public Instant getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Instant arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public List<String> getStopAirports() {
        return stopAirports;
    }

    public void setStopAirports(List<String> stopAirports) {
        this.stopAirports = stopAirports;
    }

    public List<String> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<String> flightIds) {
        this.flightIds = flightIds;
    }
}
