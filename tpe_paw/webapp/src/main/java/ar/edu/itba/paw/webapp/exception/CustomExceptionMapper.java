package ar.edu.itba.paw.webapp.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Provider
public class CustomExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(prepareMessage(exception))
                .type("text/plain")
                .build();
    }

    private String prepareMessage(ConstraintViolationException exception) {
        JsonObjectBuilder jsonObjectBuilderErrors = Json.createObjectBuilder();
        String propName, message = "Invalid value";
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            // Getting the name of the field
            propName = this.getPropertyName(cv);
            try {
                // Getting the message
                message = this.messageSource.getMessage(cv.getMessageTemplate().substring(1, cv.getMessageTemplate().length() - 1),null, LocaleContextHolder.getLocale());
            } catch (NoSuchMessageException e) {
                message = cv.getMessage();
            } finally {
                // Adding the response to the JSON object
                jsonObjectBuilderErrors.add(propName, message);
            }
        }
        JsonArrayBuilder jsonArrayBuilderErrors = Json.createArrayBuilder();
        jsonArrayBuilderErrors.add(jsonObjectBuilderErrors);
        return Json.createObjectBuilder()
                .add("errors", jsonArrayBuilderErrors)
                .build()
                .toString();
    }

    private String getPropertyName(final ConstraintViolation<?> cv) {
        final Iterator<Path.Node> iterator = cv.getPropertyPath().iterator();
        Path.Node next = null;
        while (iterator.hasNext())
            next = iterator.next();
        return next != null ? next.getName() : null;
    }
}