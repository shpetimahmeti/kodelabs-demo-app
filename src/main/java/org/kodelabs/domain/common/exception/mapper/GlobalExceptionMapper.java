package org.kodelabs.domain.common.exception.mapper;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import org.jboss.logging.Logger;
import org.kodelabs.domain.common.exception.ErrorResponse;
import org.kodelabs.domain.common.exception.NotFoundException;
import org.kodelabs.domain.flight.exception.InvalidFlightStatusTransitionException;
import org.kodelabs.domain.reservation.exception.SeatNotAvailableException;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        LOG.error("Exception handled globally: ", exception);

        return switch (exception) {
            case InvalidFlightStatusTransitionException ex -> handleInvalidFlightStatusTransition(ex);
            case SeatNotAvailableException ex -> handleSeatNotAvailable(ex);
            case NotFoundException ex -> handleNotFound(ex);
            case null, default -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Unexpected error", 500))
                    .build();
        };
    }

    public Response handleInvalidFlightStatusTransition(InvalidFlightStatusTransitionException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid status transition to: " + ex.getToStatus(),
                409
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }

    public Response handleSeatNotAvailable(SeatNotAvailableException ex) {
        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse("Requested seat is not available", 409))
                .build();
    }

    public Response handleNotFound(NotFoundException ex) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(ex.getMessage(), 404))
                .build();
    }
}
