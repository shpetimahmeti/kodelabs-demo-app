package org.kodelabs.domain.reservation.entity;

import org.kodelabs.domain.common.BaseEntity;

public class ReservationEntity extends BaseEntity {
    private String userId;
    private String flightId;
    private String seatNumber;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }
}
