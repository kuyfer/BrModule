package cires.bemodule.exceptions.email;

/**
 * Indicates that an SMTP connection or operation timed out.
 * <p>
 * Mapped to HTTP 503 Service Unavailable or 500 Internal Server Error
 * because this is a server-side infrastructure issue.
 * </p>
 */
public class SmtpTimeoutException extends EmailSendingException{

    public SmtpTimeoutException(String message) {super(message);}

    public SmtpTimeoutException(String message, Throwable cause) {super(message, cause);}
}