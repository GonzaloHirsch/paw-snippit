package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Exception for expired tokens
 */
public class ExpiredAuthenticationTokenException extends AuthenticationServiceException {
    public ExpiredAuthenticationTokenException(String msg) {
        super(msg);
    }
}
