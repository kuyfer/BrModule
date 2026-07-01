package cires.bemodule.exceptions.imports;

import cires.bemodule.exceptions.business.ImportException;

/**
 * Indicates that an import file has structural issues.
 * <p>
 * Mapped to HTTP 422 Unprocessable Content by
 * {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 * <p>
 * Typical use cases: missing headers, wrong column count, invalid file format.
 * </p>
 */
public class ImportValidationException extends ImportException {

    public ImportValidationException(String message) {super(message);}

    public ImportValidationException(String message, Throwable cause) {super(message, cause);}
}