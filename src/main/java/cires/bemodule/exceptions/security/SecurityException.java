package cires.bemodule.exceptions.security;

import org.springframework.http.HttpStatus;

/**
 * Base exception for security-related errors.
 * <p>
 * Carries an {@link HttpStatus} and an {@code errorCode} for consistent handling
 * by {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 */
public class SecurityException extends RuntimeException {

    private final HttpStatus status;
    private final String errorCode;

    protected SecurityException(String message, HttpStatus status, String errorCode) {
        super(message);
        this.status = status;
        this.errorCode = errorCode;
    }

    protected SecurityException(String message, Throwable cause, HttpStatus status, String errorCode) {
        super(message, cause);
        this.status = status;
        this.errorCode = errorCode;
    }

    public HttpStatus getStatus() { return status; }
    public String getErrorCode() { return errorCode; }
}