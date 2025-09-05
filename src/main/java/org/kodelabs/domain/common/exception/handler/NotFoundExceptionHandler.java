package org.kodelabs.domain.common.exception.handler;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.kodelabs.domain.common.exception.NotFoundException;

import org.jboss.logging.Logger;

@Provider
public class NotFoundExceptionHandler implements ExceptionMapper<NotFoundException> {

    private static final Logger LOG = Logger.getLogger(NotFoundExceptionHandler.class);

    @Override
    public Response toResponse(NotFoundException ex) {
        LOG.error("Exception handled globally: ", ex);

        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(ex.getMessage(), 404))
                .build();
    }

    public record ErrorResponse(String message, int code) {
    }
}
