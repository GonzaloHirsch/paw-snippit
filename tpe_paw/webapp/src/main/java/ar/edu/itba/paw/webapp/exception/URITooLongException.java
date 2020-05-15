package ar.edu.itba.paw.webapp.exception;

public class URITooLongException extends RuntimeException {
    public URITooLongException(String message) {
        super(message);
    }
    public URITooLongException(String message, Throwable cause) {
        super(message, cause);
    }
    public URITooLongException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
