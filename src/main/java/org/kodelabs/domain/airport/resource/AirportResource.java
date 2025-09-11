package org.kodelabs.domain.airport.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.airport.dto.AirportDTO;
import org.kodelabs.domain.airport.service.AirportService;
import org.kodelabs.domain.common.dto.PaginatedResponse;
import org.kodelabs.domain.common.dto.PaginationQueryParams;

@Path("/airports")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AirportResource {

    @Inject
    AirportService service;

    @GET
    public Uni<PaginatedResponse<AirportDTO>> findAll(@Valid @BeanParam PaginationQueryParams params) {
        return service.findAll(params);
    }

    @GET
    @Path("/{iata}")
    public Uni<AirportDTO> findById(@PathParam("iata") String iata) {
        return service.findOneByIata(iata);
    }

    //TODO cache results
}
