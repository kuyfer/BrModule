package cires.bemodule.enums;

/**
 * Represents the time slot within a day for which attendance is recorded.
 * <p>
 * Each session day is divided into two slots: morning (AM) and afternoon (PM).
 * </p>
 */
public enum AttendanceSlot {

    /** Morning slot (before midday). */
    AM,

    /** Afternoon slot (after midday). */
    PM
}