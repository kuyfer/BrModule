package cires.bemodule.enums;

/**
 * Represents the processing status of an export job.
 */
public enum ExportStatus {

    /** The export has been requested but not yet processed. */
    PENDING,

    /** The export completed successfully. */
    SUCCESS,

    /** The export failed due to an error. */
    FAILED
}