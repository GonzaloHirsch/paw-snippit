package ar.edu.itba.paw.webapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class RemovingLanguageInUseException extends RuntimeException {
    private boolean hasMessage;

    public RemovingLanguageInUseException() {
        super();
        this.hasMessage = false;
    }

    public RemovingLanguageInUseException(String message) {
        super(message);
        this.hasMessage = true;
    }

    public RemovingLanguageInUseException(String message, Throwable cause) {
        super(message, cause);
        this.hasMessage = true;
    }

    public RemovingLanguageInUseException(Throwable cause) {
        super(cause);
        this.hasMessage = false;
    }

    public RemovingLanguageInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.hasMessage = true;
    }

    public boolean hasMessage() {
        return hasMessage;
    }
}
