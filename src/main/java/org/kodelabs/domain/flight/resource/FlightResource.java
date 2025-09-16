package org.kodelabs.domain.flight.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.flight.dto.FlightAvailabilityResponse;
import org.kodelabs.domain.flight.dto.FlightRouteResponse;
import org.kodelabs.domain.flight.dto.UpdateFlightStatusRequest;
import org.kodelabs.domain.flight.service.FlightService;
import org.kodelabs.domain.flight.dto.FlightDTO;
import org.kodelabs.domain.flight.dto.RouteSearchParams;

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

    @GET
    @Path("/{id}/availability")
    public Uni<FlightAvailabilityResponse> getSeatAvailability(@PathParam("id") String id) {
        return flightService.getSeatAvailability(id);
    }
    
    @GET
    @Path("/routes")
    public Uni<List<FlightRouteResponse>> findRoutes(@Valid @BeanParam RouteSearchParams params) {
        return flightService.findConnectionsFromOriginToDestination(
                params.getOrigin(),
                params.getDestination(),
                params.getDepartureDate(),
                params.getDepartureDate().plusDays(1)).collect().asList();
    }

    @PUT
    @Path("/{id}/status")
    public Uni<FlightDTO> updateFlightStatus(@PathParam("id") String id, @Valid UpdateFlightStatusRequest request) {
        return flightService.updateFlightStatus(id, request);
    }
}


