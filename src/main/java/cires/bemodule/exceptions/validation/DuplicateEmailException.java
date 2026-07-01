package cires.bemodule.exceptions.validation;

import cires.bemodule.exceptions.business.ConflictException;

/**
 * Indicates that an email address already exists in the system.
 * <p>
 * Mapped to HTTP 409 Conflict via {@link ConflictException}.
 * </p>
 */
public class DuplicateEmailException extends ConflictException {

    public DuplicateEmailException(String message) {super(message);}

    public DuplicateEmailException(String message, Throwable cause) { super(message, cause); }
}