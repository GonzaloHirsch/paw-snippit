package ar.edu.itba.paw.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class RemovingLanguageInUseException extends RuntimeException {
    public RemovingLanguageInUseException() {
        super();
    }

    public RemovingLanguageInUseException(String message) {
        super(message);
    }

    public RemovingLanguageInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemovingLanguageInUseException(Throwable cause) {
        super(cause);
    }

    public RemovingLanguageInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
