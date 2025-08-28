package org.kodelabs.domain.route;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;

public class RouteSearchParams {
    @NotNull(message = "origin is required")
    @QueryParam("origin")
    public String origin;

    @NotNull(message = "destination is required")
    @QueryParam("dest")
    public String destination;

    @QueryParam("departureDate")
    public LocalDate departureDate;
}
