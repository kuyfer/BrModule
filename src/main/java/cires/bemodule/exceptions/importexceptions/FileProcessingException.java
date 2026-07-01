package cires.bemodule.exceptions.importexceptions;

public class FileProcessingException extends ImportException {
    public FileProcessingException(String message) {
        super(message);
    }
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}