package cires.bemodule.enums;

/**
 * Possible statuses for a participant's attendance record.
 * <p>
 * Used when marking attendance (AM/PM slots) for a specific session date.
 * </p>
 */
public enum AttendanceStatus {

    /** The participant was present. */
    PRESENT,

    /** The participant arrived late but was still present. */
    LATE,

    /** The participant was absent without justification. */
    ABSENT,

    /** The participant was absent with a valid justification. */
    JUSTIFIED_ABSENCE
}