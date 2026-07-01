package cires.bemodule.exceptions.imports;

import cires.bemodule.dtos.imports.ImportResult;

/**
 * Indicates a validation or parsing error on a single row during import.
 * <p>
 * This exception is <strong>caught internally</strong> by the import pipeline and
 * aggregated into the {@link ImportResult#getErrors()} list.
 * It does <strong>not</strong> reach the global exception handler.
 * </p>
 */
public class ImportRowException extends RuntimeException {

    public ImportRowException(String message) {
        super(message);
    }

    public ImportRowException(String message, Throwable cause) {super(message, cause);}
}