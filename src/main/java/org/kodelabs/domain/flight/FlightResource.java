package org.kodelabs.domain.flight;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kodelabs.domain.route.RouteEntity;
import org.kodelabs.domain.route.RouteSearchParams;
import org.kodelabs.domain.route.RouteService;
import org.kodelabs.domain.segment.SegmentEntity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Path("/flights")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FlightResource {

    @Inject
    RouteService routeService;

    @GET
    @Path("/first")
    public Uni<RouteEntity> getFirstRoutes() {
        return routeService.getFirstRoute();
    }

    @GET
    public Uni<List<RouteEntity>> searchRoutes(@Valid @BeanParam RouteSearchParams params) {
        return routeService.getAllRoutes(params.origin, params.destination, params.departureDate);
    }

    @GET
    @Path("/segment")
    public Uni<SegmentEntity> getFirstSegment() {
        return routeService.getFirstSegment();
    }

    @GET
    @Path("/segment/all")
    public Uni<List<SegmentEntity>> getAllSegments() {
        return routeService.getAllSegments(
                "PRN",
                "LAX",
                LocalDate.parse("2025-09-05"),
                LocalDate.parse("2025-09-06")
        );
    }
}


