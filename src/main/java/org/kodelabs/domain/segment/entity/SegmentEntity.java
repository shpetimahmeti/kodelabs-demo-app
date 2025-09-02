package org.kodelabs.domain.segment.entity;

import org.bson.types.ObjectId;
import org.kodelabs.domain.segment.model.Airline;
import org.kodelabs.domain.segment.model.SegmentPrice;

import java.time.Instant;
import java.util.List;

public class SegmentEntity {

    private ObjectId id;

    private String segmentId;
    private ObjectId routeInstanceId;
    private String routeTemplateId;

    private Airline airline;

    private String from;
    private String to;

    private Instant departureTime;
    private Instant arrivalTime;

    private List<String> flightIds;

    private int stops;

    private SegmentPrice price;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getSegmentId() {
        return segmentId;
    }

    public void setSegmentId(String segmentId) {
        this.segmentId = segmentId;
    }

    public ObjectId getRouteInstanceId() {
        return routeInstanceId;
    }

    public void setRouteInstanceId(ObjectId routeInstanceId) {
        this.routeInstanceId = routeInstanceId;
    }

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

    public List<String> getFlightIds() {
        return flightIds;
    }

    public void setFlightIds(List<String> flightIds) {
        this.flightIds = flightIds;
    }

    public SegmentPrice getPrice() {
        return price;
    }

    public void setPrice(SegmentPrice price) {
        this.price = price;
    }
}
