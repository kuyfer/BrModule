package cires.bemodule.enums;

/**
 * Types of system notifications that can be sent to users.
 * <p>
 * Each type corresponds to a specific event in the application lifecycle
 * and may trigger an email via the notification/email queue.
 * </p>
 */
public enum NotificationType {

    /** Sent when a new user account is created. */
    ACCOUNT_CREATION,

    /** Sent when a user requests a password reset. */
    PASSWORD_RESET,

    /** Sent to a trainer when they are assigned to a session. */
    TRAINER_ASSIGNMENT,

    /** Reminder sent before a scheduled session starts. */
    SESSION_REMINDER,

    /** Sent when a session is cancelled. */
    SESSION_CANCELLATION,

    /** Sent when a day's attendance has been validated by the trainer. */
    ATTENDANCE_VALIDATED,

    /** Sent when a session is postponed to a new date. */
    SESSION_POSTPONED,

    /** Sent to a new user to set up their initial password. */
    PASSWORD_SETUP,

    /** Sent when a report export is ready for download. */
    EXPORT_READY
}