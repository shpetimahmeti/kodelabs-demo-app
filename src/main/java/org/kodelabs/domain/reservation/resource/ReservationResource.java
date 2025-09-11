package org.kodelabs.domain.reservation.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.common.dto.PaginatedResponse;
import org.kodelabs.domain.common.dto.PaginationQueryParams;
import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.dto.ReservationDTO;
import org.kodelabs.domain.reservation.service.ReservationService;

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

    @POST
    public Uni<ReservationDTO> crateReservation(@Valid CreateReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }

    @GET
    @Path("/users/{userId}")
    public Uni<PaginatedResponse<ReservationDTO>> findByUserId(@PathParam("userId") String userId,
                                                               @Valid @BeanParam PaginationQueryParams params) {
        return reservationService.findByUserId(userId, params.page, params.size, params.ascending);
    }
}
