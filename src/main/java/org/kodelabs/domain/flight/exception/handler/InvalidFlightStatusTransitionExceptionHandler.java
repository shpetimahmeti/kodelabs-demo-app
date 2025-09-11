package org.kodelabs.domain.flight.exception.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.kodelabs.domain.common.exception.ErrorResponse;
import org.kodelabs.domain.flight.exception.InvalidFlightStatusTransitionException;

@Provider
public class InvalidFlightStatusTransitionExceptionHandler implements ExceptionMapper<InvalidFlightStatusTransitionException> {

    private static final Logger LOG = Logger.getLogger(InvalidFlightStatusTransitionExceptionHandler.class);

    @Override
    public Response toResponse(InvalidFlightStatusTransitionException ex) {

        LOG.error("Exception: ", ex);

        ErrorResponse errorResponse = new ErrorResponse(
                "Invalid status transition to: " + ex.getToStatus(),
                409
        );
        return Response.status(Response.Status.CONFLICT)
                .entity(errorResponse)
                .build();
    }
}