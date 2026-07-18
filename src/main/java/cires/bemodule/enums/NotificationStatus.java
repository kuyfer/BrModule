package cires.bemodule.enums;

/**
 * Represents the delivery status of a notification.
 * <p>
 * The status is updated by the email queue consumer after attempting to send
 * the email.
 * </p>
 */
public enum NotificationStatus {

    /** The notification has been created but not yet processed. */
    PENDING,

    /** The notification was processed and the email was sent successfully. */
    SENT,

    /** The notification processing failed (e.g., email could not be sent). */
    FAILED
}