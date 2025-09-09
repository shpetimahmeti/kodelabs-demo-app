package org.kodelabs.domain.reservation.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.PaginatedResponse;
import org.kodelabs.domain.common.pagination.PaginationMapper;
import org.kodelabs.domain.common.transaction.TransactionHelper;
import org.kodelabs.domain.flight.repository.FlightRepository;
import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationDTO;
import org.kodelabs.domain.reservation.entity.ReservationEntity;
import org.kodelabs.domain.reservation.exception.ReservationNotFoundException;
import org.kodelabs.domain.reservation.exception.SeatNotAvailableException;
import org.kodelabs.domain.reservation.mapper.ReservationMapper;
import org.kodelabs.domain.reservation.repository.ReservationRepository;

import java.time.Instant;
import java.util.List;

@ApplicationScoped
public class ReservationService {

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    FlightRepository flightRepository;

    @Inject
    TransactionHelper transactionHelper;

    public Uni<ReservationEntity> findByObjectId(String id) {
        return reservationRepository.findByObjectId(id)
                .onItem().ifNull().failWith(new ReservationNotFoundException(id));
    }

    public Uni<PaginatedResponse<ReservationDTO>> findByUserId(String userId, int page, int size, boolean ascending) {
        return reservationRepository.findByUserId(userId, page, size, ascending)
                .map(result ->
                        PaginationMapper.toPaginatedResponse(result, page, size, ReservationMapper::toDto));
    }

    public Uni<ReservationEntity> createReservation(CreateReservationDTO reservationDTO) {
        return transactionHelper.inTransaction((session -> {
            ReservationEntity entity = ReservationMapper.toEntity(reservationDTO);

            return flightRepository
                    .reserveSeat(session, reservationDTO.flightId, reservationDTO.seatNumber)
                    .flatMap(updateResult -> {
                        if (updateResult.getModifiedCount() == 0) {
                            return Uni.createFrom().failure(new SeatNotAvailableException(entity.getSeatNumber()));
                        }

                        entity.generateId();
                        entity.setCreatedAt(Instant.now());
                        entity.setUpdatedAt(Instant.now());

                        return reservationRepository.insertReservation(session, entity).replaceWith(entity);
                    });
        }));
    }
}
