package cires.bemodule.exceptions.imports;

import cires.bemodule.exceptions.business.ImportException;

/**
 * Indicates that an import file is corrupt, malformed, or cannot be read.
 * <p>
 * Mapped to HTTP 422 Unprocessable Content by
 * {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 */
public class FileProcessingException extends ImportException {

    public FileProcessingException(String message) {super(message);}

    public FileProcessingException(String message, Throwable cause) {super(message, cause);}
}