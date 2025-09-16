package org.kodelabs.domain.reservation.dto;

import org.kodelabs.domain.common.dto.BaseDTO;
import org.kodelabs.domain.reservation.enums.ReservationStatus;

import java.time.Instant;

public class ReservationDTO extends BaseDTO {

    private String userId;
    private String flightId;
    private String seatNumber;
    private Instant bookingDate;
    private ReservationStatus status;

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

    public Instant getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }
}
