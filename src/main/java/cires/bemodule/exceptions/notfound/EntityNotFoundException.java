package cires.bemodule.exceptions.notfound;

/**
 * Base exception for all "entity not found" errors.
 * <p>
 * Mapped to HTTP 404 Not Found by {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 * <p>
 * All specific {@code *NotFoundException} classes should extend this class.
 * </p>
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) { super(message, cause); }
}