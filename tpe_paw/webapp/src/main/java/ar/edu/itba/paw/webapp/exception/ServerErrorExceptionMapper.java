package ar.edu.itba.paw.webapp.exception;

import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Param exception mapper, in case there is a server error.
 * Empty entity to avoid Tomcat body
 */
@Provider
public class ServerErrorExceptionMapper implements ExceptionMapper<ServerErrorException> {
    @Override
    public Response toResponse(ServerErrorException e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("").build();
    }
}
