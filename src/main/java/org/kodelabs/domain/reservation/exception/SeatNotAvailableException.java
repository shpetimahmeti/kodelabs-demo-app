package org.kodelabs.domain.reservation.exception;

public class SeatNotAvailableException extends RuntimeException {
    public SeatNotAvailableException(String seatNumber) {
        super("Seat " + seatNumber + " is not available");
    }
}
