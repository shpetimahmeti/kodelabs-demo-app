package org.kodelabs.domain.flight;

import org.bson.types.ObjectId;
import org.kodelabs.domain.flight.dto.Leg;

import java.time.Instant;
import java.util.List;

public class FlightEntity {

    private ObjectId id;
    private String routeId;
    private int routeVersion;
    private String flightNumber;
    private Instant date;

    private String originIata;
    private String originIcao;
    private String destinationIata;
    private String destinationIcao;
    private String originTimezone;
    private String destinationTimezone;

    private String overallStatus;
    private List<Leg> legs;

    private int totalAvailableSeats;
    private Instant createdAt;
    private Instant updatedAt;

    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getRouteId() { return routeId; }
    public void setRouteId(String routeId) { this.routeId = routeId; }

    public int getRouteVersion() { return routeVersion; }
    public void setRouteVersion(int routeVersion) { this.routeVersion = routeVersion; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }

    public String getOriginIata() { return originIata; }
    public void setOriginIata(String originIata) { this.originIata = originIata; }

    public String getOriginIcao() { return originIcao; }
    public void setOriginIcao(String originIcao) { this.originIcao = originIcao; }

    public String getDestinationIata() { return destinationIata; }
    public void setDestinationIata(String destinationIata) { this.destinationIata = destinationIata; }

    public String getDestinationIcao() { return destinationIcao; }
    public void setDestinationIcao(String destinationIcao) { this.destinationIcao = destinationIcao; }

    public String getOriginTimezone() { return originTimezone; }
    public void setOriginTimezone(String originTimezone) { this.originTimezone = originTimezone; }

    public String getDestinationTimezone() { return destinationTimezone; }
    public void setDestinationTimezone(String destinationTimezone) { this.destinationTimezone = destinationTimezone; }

    public String getOverallStatus() { return overallStatus; }
    public void setOverallStatus(String overallStatus) { this.overallStatus = overallStatus; }

    public List<Leg> getLegs() { return legs; }
    public void setLegs(List<Leg> legs) { this.legs = legs; }

    public int getTotalAvailableSeats() { return totalAvailableSeats; }
    public void setTotalAvailableSeats(int totalAvailableSeats) { this.totalAvailableSeats = totalAvailableSeats; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
