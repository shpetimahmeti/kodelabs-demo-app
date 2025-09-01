package org.kodelabs.domain.flight;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.NotBlank;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.flight.dto.FlightDTO;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    @Inject
    FlightService flightService;

    @GET
    @Path("/{id}")
    public Uni<FlightDTO> findByObjectId(@PathParam("id")
                                         @NotBlank(message = "id is required") String id) {
        return flightService.findOneByObjectId(id);
    }

    //TODO implement find by object id and departure date
    //TODO implement to search by multiple object ids
}


