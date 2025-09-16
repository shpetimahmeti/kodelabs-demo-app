package org.kodelabs.domain.reservation.entity;

import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.kodelabs.domain.common.entity.BaseEntity;
import org.kodelabs.domain.reservation.enums.ReservationStatus;

public class ReservationEntity extends BaseEntity {
    private String userId;
    private String flightId;
    private String seatNumber;
    public String status;

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @BsonIgnore
    public ReservationStatus getStatusEnum() {
        return status == null ? ReservationStatus.UNKNOWN : ReservationStatus.valueOf(status);
    }

    @BsonIgnore
    public void setStatusEnum(ReservationStatus statusEnum) {
        this.status = statusEnum.name();
    }
}
