package ar.edu.itba.paw.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    private boolean hasMessage;

    public UserNotFoundException() {
        super();
        this.hasMessage = false;
    }

    public UserNotFoundException(String message) {
        super(message);
        this.hasMessage = true;
    }

    public UserNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.hasMessage = true;
    }

    public UserNotFoundException(Throwable cause) {
        super(cause);
        this.hasMessage = false;
    }

    public UserNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.hasMessage = true;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}
