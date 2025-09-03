package org.kodelabs.domain.flight.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.flight.dto.FlightRouteResponse;
import org.kodelabs.domain.flight.dto.FlightWithConnections;
import org.kodelabs.domain.flight.service.FlightService;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.route.dto.RouteSearchParams;

import java.util.List;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    @Inject
    FlightService flightService;

    @GET
    @Path("/{id}")
    public Uni<FlightDTO> findByObjectId(@PathParam("id") String id) {
        return flightService.findOneByObjectId(id);
    }

    //TODO use instant directly?
    @GET
    @Path("/routes")
    public Uni<List<FlightRouteResponse>> findRoutes(@Valid @BeanParam RouteSearchParams params) {
        return flightService.findConnectionsFromOriginToDestination(
                params.getOrigin(),
                params.getDestination(),
                params.getDepartureDate(),
                params.getDepartureDate().plusDays(1)).collect().asList();
    }

    //TODO implement find by object id and departure date
    //TODO implement to search by multiple object ids
}


