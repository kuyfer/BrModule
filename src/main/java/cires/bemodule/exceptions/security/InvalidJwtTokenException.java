package cires.bemodule.exceptions.security;

import org.springframework.http.HttpStatus;

/**
 * Indicates that a JWT token is malformed, expired, or otherwise invalid.
 * <p>
 * Mapped to HTTP 401 Unauthorized with error code {@code INVALID_TOKEN}.
 * </p>
 */
public class InvalidJwtTokenException extends SecurityException {

    public InvalidJwtTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }

    public InvalidJwtTokenException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }
}