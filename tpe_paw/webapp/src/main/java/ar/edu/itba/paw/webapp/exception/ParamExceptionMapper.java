package ar.edu.itba.paw.webapp.exception;

import org.glassfish.jersey.server.ParamException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Param exception mapper, in case there is an error in a parameter.
 * Empty entity to avoid Tomcat body
 */
@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {
    @Override
    public Response toResponse(ParamException e) {
        return Response.status(Response.Status.NOT_FOUND).entity("").build();
    }
}
