package org.kodelabs.domain.flight.entity;

import org.kodelabs.domain.common.entity.BaseEntity;
import org.kodelabs.domain.flight.model.Place;
import org.kodelabs.domain.flight.model.Seat;

import java.time.Instant;
import java.util.List;

public class FlightEntity extends BaseEntity {

    private String publishedFlightNumber;
    private String operatingFlightNumber;
    private Place from;
    private Place to;
    private Instant departureTime;
    private Instant arrivalTime;
    private String aircraftRegistration;
    private String aircraftType;
    private String status;
    private int availableSeatsCount;
    private List<Seat> seats;
    private int price;

    public String getPublishedFlightNumber() {
        return publishedFlightNumber;
    }

    public void setPublishedFlightNumber(String publishedFlightNumber) {
        this.publishedFlightNumber = publishedFlightNumber;
    }

    public int getAvailableSeatsCount() {
        return availableSeatsCount;
    }

    public void setAvailableSeatsCount(int availableSeatsCount) {
        this.availableSeatsCount = availableSeatsCount;
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

    public String getOperatingFlightNumber() {
        return operatingFlightNumber;
    }

    public void setOperatingFlightNumber(String operatingFlightNumber) {
        this.operatingFlightNumber = operatingFlightNumber;
    }

    public Place getFrom() {
        return from;
    }

    public void setFrom(Place from) {
        this.from = from;
    }

    public Place getTo() {
        return to;
    }

    public void setTo(Place to) {
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

    public String getAircraftRegistration() {
        return aircraftRegistration;
    }

    public void setAircraftRegistration(String aircraftRegistration) {
        this.aircraftRegistration = aircraftRegistration;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    public void setSeats(List<Seat> seats) {
        this.seats = seats;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
