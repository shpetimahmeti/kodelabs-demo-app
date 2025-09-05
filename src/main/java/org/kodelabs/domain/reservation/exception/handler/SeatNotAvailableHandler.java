package org.kodelabs.domain.reservation.exception.handler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.kodelabs.domain.common.exception.ErrorResponse;
import org.kodelabs.domain.reservation.exception.SeatNotAvailableException;

@Provider
public class SeatNotAvailableHandler implements ExceptionMapper<SeatNotAvailableException> {

    private static final Logger LOG = Logger.getLogger(SeatNotAvailableHandler.class);

    @Override
    public Response toResponse(SeatNotAvailableException ex) {
        LOG.error("Exception: ", ex);

        return Response.status(Response.Status.CONFLICT)
                .entity(new ErrorResponse(ex.getMessage(), 409))
                .build();
    }
}
