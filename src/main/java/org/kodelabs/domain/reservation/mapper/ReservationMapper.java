package org.kodelabs.domain.reservation.mapper;

import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationDTO;
import org.kodelabs.domain.reservation.entity.ReservationEntity;

public class ReservationMapper {

    public static ReservationEntity toEntity(CreateReservationDTO dto) {
        ReservationEntity entity = new ReservationEntity();
        entity.setFlightId(dto.flightId);
        entity.setSeatNumber(dto.seatNumber);
        entity.setUserId(dto.userId);

        return entity;
    }

    public static ReservationDTO toDto(ReservationEntity entity) {
        ReservationDTO dto = new ReservationDTO();
        dto.setFlightId(entity.getFlightId());
        dto.setSeatNumber(entity.getSeatNumber());
        dto.setUserId(entity.getUserId());

        return dto;
    }
}
