package cires.bemodule.exceptions.email;

/**
 * Base exception for all email-related errors.
 * <p>
 * Mapped to HTTP 500 Internal Server Error by
 * {@link cires.bemodule.exceptions.GlobalExceptionHandler}.
 * </p>
 * <p>
 * Subtypes exist for specific failure scenarios:
 * <ul>
 *   <li>{@link EmptyTemplateException} – when the email template is blank</li>
 *   <li>{@link InvalidEmailAddressException} – when the recipient email is malformed</li>
 *   <li>{@link SmtpTimeoutException} – when the SMTP server times out</li>
 * </ul>
 * </p>
 */
public class EmailSendingException extends RuntimeException{

    public EmailSendingException(String message) {super(message);}

    public EmailSendingException(String message, Throwable cause) {super(message, cause);}
}