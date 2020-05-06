package ar.edu.itba.paw.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class ElementDeletionException extends RuntimeException {

    public ElementDeletionException(String message) {
        super(message);
    }

    public ElementDeletionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ElementDeletionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}