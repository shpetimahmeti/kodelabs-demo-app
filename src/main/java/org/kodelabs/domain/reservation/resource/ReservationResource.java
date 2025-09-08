package org.kodelabs.domain.reservation.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.reservation.dto.CreateReservationDTO;
import org.kodelabs.domain.reservation.entity.ReservationEntity;
import org.kodelabs.domain.reservation.service.ReservationService;

import java.util.List;

@Path("/reservations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReservationResource {

    @Inject
    ReservationService reservationService;

    @GET
    @Path("/{id}")
    public Uni<ReservationEntity> findByObjectId(@PathParam("id") String id) {
        return reservationService.findByObjectId(id);
    }

    @POST
    public Uni<ReservationEntity> crateReservation(@Valid CreateReservationDTO reservationDTO) {
        return reservationService.createReservation(reservationDTO);
    }

    @GET
    @Path("/users/{userId}")
    public Uni<List<ReservationEntity>> findByUserId(@PathParam("userId") String userId) {
        return reservationService.findByUserId(userId);
    }
}
