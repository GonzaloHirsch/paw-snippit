package ar.edu.itba.paw.webapp.exception;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Param exception mapper, in case of a METHOD NOT ALLOWED exception.
 * Empty entity to avoid Tomcat body
 */
@Provider
public class MethodNotAllowedExceptionMapper implements ExceptionMapper<NotAllowedException> {
    @Override
    public Response toResponse(NotAllowedException e) {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED).entity("").build();
    }
}
