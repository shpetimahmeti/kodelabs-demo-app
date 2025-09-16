package org.kodelabs.domain.reservation.resource;

import io.smallrye.common.constraint.NotNull;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.common.annotation.ValidSortField;
import org.kodelabs.domain.common.pagination.dto.PaginatedResponse;
import org.kodelabs.domain.common.pagination.dto.PaginationQueryParams;
import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationsPerDayResponse;
import org.kodelabs.domain.reservation.dto.TopUserReservationsResponse;
import org.kodelabs.domain.reservation.entity.ReservationEntity;
import org.kodelabs.domain.reservation.service.ReservationService;

import java.time.LocalDate;
import java.util.List;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    @Inject
    ReservationService reservationService;

    @GET
    @Path("/{id}")
    public Uni<ReservationDTO> findByObjectId(@PathParam("id") String id) {
        return reservationService.findByObjectId(id);
    }

    @GET
    @Path("/per-day")
    public Uni<List<ReservationsPerDayResponse>> reservationsPerDay(@QueryParam("from") @NotNull LocalDate from,
                                                                    @QueryParam("to") @NotNull LocalDate to) {
        return reservationService.getReservationsPerDay(from, to);
    }

    @GET
    @Path("/top")
    public Uni<List<TopUserReservationsResponse>> getTopUsers(@QueryParam("limit") @DefaultValue("10") int limit) {
        return reservationService.getTopUsers(limit);
    }

    @POST
    public Uni<ReservationDTO> crateReservation(@Valid CreateReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }

    @POST
    @Path("/{id}/cancel")
    public Uni<ReservationDTO> cancelReservation(@PathParam("id") String id) {
        return reservationService.cancelReservation(id);
    }

    @GET
    @Path("/users/{userId}")
    public Uni<PaginatedResponse<ReservationDTO>> findByUserId(@PathParam("userId") String userId,
                                                               @ValidSortField(entity = ReservationEntity.class, message = "Non-existing sorting field")
                                                               @Valid @BeanParam PaginationQueryParams params) {
        return reservationService.findByUserId(userId, params);
    }
}
