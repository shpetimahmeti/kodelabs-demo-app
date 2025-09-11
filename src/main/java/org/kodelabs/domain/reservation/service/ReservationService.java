package org.kodelabs.domain.reservation.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.kodelabs.domain.common.dto.PaginatedResponse;
import org.kodelabs.domain.common.dto.PaginationQueryParams;
import org.kodelabs.domain.common.pagination.PaginationMapper;
import org.kodelabs.domain.common.transaction.TransactionService;
import org.kodelabs.domain.flight.repository.FlightRepository;
import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationDTO;
import org.kodelabs.domain.reservation.entity.ReservationEntity;
import org.kodelabs.domain.reservation.exception.ReservationNotFoundException;
import org.kodelabs.domain.reservation.exception.SeatNotAvailableException;
import org.kodelabs.domain.reservation.mapper.ReservationMapper;
import org.kodelabs.domain.reservation.repository.ReservationRepository;

@ApplicationScoped
public class ReservationService {

    @Inject
    ReservationRepository reservationRepository;

    @Inject
    FlightRepository flightRepository;

    @Inject
    TransactionService transactionService;

    public Uni<ReservationDTO> findByObjectId(String id) {
        return reservationRepository.findByObjectId(id)
                .onItem().ifNull().failWith(new ReservationNotFoundException(id)).map(ReservationMapper::toDto);
    }

    public Uni<PaginatedResponse<ReservationDTO>> findByUserId(String userId, PaginationQueryParams params) {
        return reservationRepository.findByUserId(userId, params.page, params.size, params.sortField, params.ascending)
                .map(result ->
                        PaginationMapper.toPaginatedResponse(result, params.page, params.size, ReservationMapper::toDto));
    }

    public Uni<ReservationDTO> createReservation(CreateReservationDTO reservationDTO) {
        return transactionService.inTransaction((session -> {
            ReservationEntity entity = ReservationMapper.toEntity(reservationDTO);

            return flightRepository
                    .reserveSeat(session, reservationDTO.flightId, reservationDTO.seatNumber)
                    .flatMap(updateResult -> {
                        if (updateResult.getModifiedCount() == 0) {
                            return Uni.createFrom().failure(new SeatNotAvailableException(entity.getSeatNumber()));
                        }

                        return reservationRepository.insertOne(session, entity).replaceWith(entity).map(ReservationMapper::toDto);
                    });
        }));
    }
}
