package org.kodelabs.domain.route;

import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.QueryParam;

import java.time.LocalDate;

public class RouteSearchParams {
    @NotNull(message = "origin is required")
    @QueryParam("origin")
    private String origin;

    @NotNull(message = "destination is required")
    @QueryParam("dest")
    private String destination;

    @QueryParam("departure")
    @NotNull(message = "departure date is required")
    private LocalDate departureDate;

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }
}
