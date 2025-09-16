package org.kodelabs.domain.reservation.dto;

import java.time.LocalDate;

public class ReservationsPerDayResponse {
    private LocalDate date;
    private long reservations;

    public ReservationsPerDayResponse(LocalDate date, long reservations) {
        this.date = date;
        this.reservations = reservations;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getReservations() {
        return reservations;
    }

    public void setReservations(long reservations) {
        this.reservations = reservations;
    }
}
