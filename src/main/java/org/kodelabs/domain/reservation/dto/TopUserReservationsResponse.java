package org.kodelabs.domain.reservation.dto;

public class TopUserReservationsResponse {
    private String userId;
    private long reservationCount;

    public TopUserReservationsResponse(String userId, long reservationCount) {
        this.userId = userId;
        this.reservationCount = reservationCount;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public long getReservationCount() {
        return reservationCount;
    }

    public void setReservationCount(long reservationCount) {
        this.reservationCount = reservationCount;
    }
}
