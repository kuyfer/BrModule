package cires.bemodule.exceptions.business;

/**
 * Indicates that the client request is malformed or invalid.
 * <p>
 * Mapped to HTTP 400 Bad Request by {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 */
public class BadRequestException extends RuntimeException{

        public BadRequestException(String message) {
        super(message);
    }

        public BadRequestException(String message, Throwable cause) { super(message, cause); }
}