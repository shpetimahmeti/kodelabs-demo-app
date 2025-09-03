package org.kodelabs.domain.flight.dto;

import java.util.List;

public class FlightRouteResponse {
    private String originIata;
    private String destIata;

    private int stops;
    private int totalFlightDurationMin;
    private List<FlightDTO> legs;


    public int getStops() {
        return stops;
    }

    public void setStops(int stops) {
        this.stops = stops;
    }

    public int getTotalFlightDurationMin() {
        return totalFlightDurationMin;
    }

    public void setTotalFlightDurationMin(int totalFlightDurationMin) {
        this.totalFlightDurationMin = totalFlightDurationMin;
    }

    public List<FlightDTO> getLegs() {
        return legs;
    }

    public void setLegs(List<FlightDTO> legs) {
        this.legs = legs;
    }

    public FlightRouteResponse() {
    }

    public String getOriginIata() {
        return originIata;
    }

    public void setOriginIata(String originIata) {
        this.originIata = originIata;
    }

    public String getDestIata() {
        return destIata;
    }

    public void setDestIata(String destIata) {
        this.destIata = destIata;
    }
}
