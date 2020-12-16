package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.webapp.exception.AuthenticationMethodNotSupportedException;
import ar.edu.itba.paw.webapp.exception.ExpiredAuthenticationTokenException;
import ar.edu.itba.paw.webapp.exception.InvalidAuthenticationTokenException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.json.Json;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtLoginAuthenticationFailureHandler implements AuthenticationFailureHandler {
    @Autowired
    private ObjectMapper mapper;

    private static final String EXPIRED_TOKEN_ERROR = "TokenError";
    private static final String GENERAL_ERROR = "error";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException e) throws IOException, ServletException {

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        if (e instanceof BadCredentialsException) {
            response.getWriter().write(convertErrorMessageToJson(GENERAL_ERROR, e.getMessage()));
        } else if (e instanceof AuthenticationMethodNotSupportedException) {
            response.getWriter().write(convertErrorMessageToJson(GENERAL_ERROR, e.getMessage()));
        } else if (e instanceof ExpiredAuthenticationTokenException) {
            response.getWriter().write(convertErrorMessageToJson(EXPIRED_TOKEN_ERROR, e.getMessage()));
        } else if (e instanceof InvalidAuthenticationTokenException) {
            response.getWriter().write(convertErrorMessageToJson(GENERAL_ERROR, e.getMessage()));
        } else {
            response.getWriter().write(convertErrorMessageToJson(GENERAL_ERROR, "Authentication failed"));
        }
    }

    private String convertErrorMessageToJson(String property, String message) {
        return Json.createObjectBuilder()
                .add("error", message)
                .build()
                .toString();
    }
}
