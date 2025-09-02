package org.kodelabs.domain.route.entity;

import org.bson.types.ObjectId;

import java.time.Instant;
import java.util.List;

public class RouteEntity {
    private ObjectId id;
    private String routeTemplateId;
    private Instant date;
    private Airport origin;
    private Airport destination;
    private String overallStatus;
    private int stopCount;
    private List<Segment> segments;
    private Instant createdAt;
    private Instant updatedAt;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getRouteTemplateId() {
        return routeTemplateId;
    }

    public void setRouteTemplateId(String routeTemplateId) {
        this.routeTemplateId = routeTemplateId;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public Airport getOrigin() {
        return origin;
    }

    public void setOrigin(Airport origin) {
        this.origin = origin;
    }

    public Airport getDestination() {
        return destination;
    }

    public void setDestination(Airport destination) {
        this.destination = destination;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setOverallStatus(String overallStatus) {
        this.overallStatus = overallStatus;
    }

    public int getStopCount() {
        return stopCount;
    }

    public void setStopCount(int stopCount) {
        this.stopCount = stopCount;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public void setSegments(List<Segment> segments) {
        this.segments = segments;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public static class Airport {
        private String iata;
        private String icao;
        private String timezone;

        public String getIata() {
            return iata;
        }

        public void setIata(String iata) {
            this.iata = iata;
        }

        public String getIcao() {
            return icao;
        }

        public void setIcao(String icao) {
            this.icao = icao;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }
    }

    public static class Segment {
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

}
