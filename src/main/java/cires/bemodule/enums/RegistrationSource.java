package cires.bemodule.enums;

/**
 * Indicates how a participant was registered in the system.
 */
public enum RegistrationSource {

    /** Registered manually by an administrator or trainer via the UI. */
    MANUAL,

    /** Imported in bulk through a file import (CSV or Excel). */
    IMPORT
}