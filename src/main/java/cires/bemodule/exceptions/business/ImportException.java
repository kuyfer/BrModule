package cires.bemodule.exceptions.business;

/**
 * Base exception for all import-related errors.
 * <p>
 * Subtypes are handled individually by {@link cires.bemodule.exceptions.GlobalExceptionHandler}
 * and mapped to HTTP 422 Unprocessable Content.
 * </p>
 *
 * @see cires.bemodule.exceptions.imports.ImportValidationException
 * @see cires.bemodule.exceptions.imports.FileProcessingException
 * @see cires.bemodule.exceptions.imports.ImportRowException
 */
public class ImportException extends RuntimeException {

    public ImportException(String message) {
        super(message);
    }

    public ImportException(String message, Throwable cause) {super(message, cause);
    }
}