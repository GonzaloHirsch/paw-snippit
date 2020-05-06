package ar.edu.itba.paw.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SnippetNotFoundException extends RuntimeException {

    public SnippetNotFoundException(String message) {
        super(message);
    }

    public SnippetNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public SnippetNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}