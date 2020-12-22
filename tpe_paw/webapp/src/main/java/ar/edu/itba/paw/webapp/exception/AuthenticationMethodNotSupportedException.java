package ar.edu.itba.paw.webapp.exception;

import org.springframework.security.authentication.AuthenticationServiceException;

/**
 * Exception to be thrown if the given method for login is not the allowed one
 */
public class AuthenticationMethodNotSupportedException extends AuthenticationServiceException {
    public AuthenticationMethodNotSupportedException(String msg) {
        super(msg);
    }
}
