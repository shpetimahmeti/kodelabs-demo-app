package org.kodelabs.domain.reservation.exception;

import org.kodelabs.domain.common.exception.NotFoundException;

public class ReservationNotFoundException extends NotFoundException {
    public ReservationNotFoundException(String reservationId) {
        super("Reservation not found: " + reservationId);
    }
}
