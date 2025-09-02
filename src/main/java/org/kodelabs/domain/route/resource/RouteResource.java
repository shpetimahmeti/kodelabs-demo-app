package org.kodelabs.domain.route.resource;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.route.dto.RouteSearchParams;
import org.kodelabs.domain.route.dto.RouteSearchResponse;
import org.kodelabs.domain.route.service.RouteService;

import java.util.List;

@Path("/routes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RouteResource {

    @Inject
    RouteService routeService;

    @GET
    public Uni<List<RouteSearchResponse>> getAllSegments(@Valid @BeanParam RouteSearchParams params) {
        return routeService.findRoutes(
                params.getOrigin(),
                params.getDestination(),
                params.getDepartureDate(),
                params.getDepartureDate().plusDays(1)
        ).collect().asList();
    }
}
