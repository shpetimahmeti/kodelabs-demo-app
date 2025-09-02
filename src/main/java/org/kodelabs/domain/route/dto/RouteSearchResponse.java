package org.kodelabs.domain.route.dto;

import org.kodelabs.domain.segment.model.SegmentPrice;

import java.time.Instant;
import java.util.List;


/***
 * Represents a full path of connected direct segments,
 * from the origin to the destination
 */
public class RouteSearchResponse {

    private String origin;
    private String destination;
    private int stops;
    private int totalFlightDurationMin;
    private SegmentPrice totalPrice;
    private boolean isSelfTransfer;
    private List<Leg> legs;
    private List<Group> groups;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

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

    public SegmentPrice getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(SegmentPrice totalPrice) {
        this.totalPrice = totalPrice;
    }


    public List<Leg> getLegs() {
        return legs;
    }

    public void setLegs(List<Leg> legs) {
        this.legs = legs;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public boolean isSelfTransfer() {
        return isSelfTransfer;
    }

    public void setSelfTransfer(boolean selfTransfer) {
        isSelfTransfer = selfTransfer;
    }

    public static class Group {
        private String groupId;
        private String routeReferenceId;
        private String groupType;
        private List<String> legs;

        public String getGroupId() {
            return groupId;
        }

        public void setGroupId(String groupId) {
            this.groupId = groupId;
        }

        public String getRouteReferenceId() {
            return routeReferenceId;
        }

        public void setRouteReferenceId(String routeReferenceId) {
            this.routeReferenceId = routeReferenceId;
        }

        public String getGroupType() {
            return groupType;
        }

        public void setGroupType(String groupType) {
            this.groupType = groupType;
        }

        public List<String> getLegs() {
            return legs;
        }

        public void setLegs(List<String> legs) {
            this.legs = legs;
        }
    }

    public static class Leg {
        private String legId;
        private String routeReferenceId;
        private String origin;
        private String destination;
        private String publishedFlightNumber;
        private String operatingFlightNumber;
        private Instant departureTime;
        private Instant arrivalTime;
        private Integer layoverMinutesToNext;

        public String getLegId() {
            return legId;
        }

        public void setLegId(String legId) {
            this.legId = legId;
        }

        public String getRouteReferenceId() {
            return routeReferenceId;
        }

        public void setRouteReferenceId(String routeReferenceId) {
            this.routeReferenceId = routeReferenceId;
        }

        public String getOrigin() {
            return origin;
        }

        public void setOrigin(String origin) {
            this.origin = origin;
        }

        public String getDestination() {
            return destination;
        }

        public void setDestination(String destination) {
            this.destination = destination;
        }

        public String getPublishedFlightNumber() {
            return publishedFlightNumber;
        }

        public void setPublishedFlightNumber(String publishedFlightNumber) {
            this.publishedFlightNumber = publishedFlightNumber;
        }

        public String getOperatingFlightNumber() {
            return operatingFlightNumber;
        }

        public void setOperatingFlightNumber(String operatingFlightNumber) {
            this.operatingFlightNumber = operatingFlightNumber;
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

        public Integer getLayoverMinutesToNext() {
            return layoverMinutesToNext;
        }

        public void setLayoverMinutesToNext(Integer layoverMinutesToNext) {
            this.layoverMinutesToNext = layoverMinutesToNext;
        }
    }
}
