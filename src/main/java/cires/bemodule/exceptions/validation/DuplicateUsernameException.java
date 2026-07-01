package cires.bemodule.exceptions.validation;

import cires.bemodule.exceptions.business.ConflictException;

/**
 * Indicates that a username already exists in the system.
 * <p>
 * Mapped to HTTP 409 Conflict via {@link ConflictException}.
 * </p>
 */
public class DuplicateUsernameException extends ConflictException {

    public DuplicateUsernameException(String message) {super(message);}

    public DuplicateUsernameException(String message, Throwable cause) { super(message, cause); }
}