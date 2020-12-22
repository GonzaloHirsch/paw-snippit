package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Exception for invalid tokens
 */
public class InvalidAuthenticationTokenException extends AuthenticationServiceException {
    public InvalidAuthenticationTokenException(String msg) {
        super(msg);
    }
}
