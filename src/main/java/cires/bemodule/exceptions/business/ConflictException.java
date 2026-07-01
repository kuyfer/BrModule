package cires.bemodule.exceptions.business;

/**
 * Indicates that the request conflicts with the current state of the resource.
 * <p>
 * Mapped to HTTP 409 Conflict by {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 * <p>
 * Typical use cases: duplicate email, duplicate username, or business rule violation.
 * </p>
 */
public class ConflictException extends RuntimeException{

        public ConflictException(String message) {super(message);}

        public ConflictException(String message, Throwable cause) {super(message, cause);}
}