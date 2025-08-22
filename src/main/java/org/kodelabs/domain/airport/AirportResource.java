package org.kodelabs.domain.airport;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.common.PaginatedResponse;

@Path("/airports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AirportResource {

    @Inject
    AirportService service;

    @GET
    public Uni<PaginatedResponse<AirportDTO>> findAll(@QueryParam("page") @DefaultValue("0") @Min(0) int page,
                                                      @QueryParam("size") @DefaultValue("20") @Min(1) int size,
                                                      @QueryParam("asc") @DefaultValue("true") boolean ascending) {
        return service.findAll(page, size, ascending);
    }

    @GET
    @Path("/{iata}")
    public Uni<AirportDTO> findById(@PathParam("iata") String iata) {
        return service.findOneByIata(iata);
    }
}
