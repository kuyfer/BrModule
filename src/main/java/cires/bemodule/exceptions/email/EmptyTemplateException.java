package cires.bemodule.exceptions.email;

/**
 * Indicates that an email template is empty or missing required content.
 * <p>
 * Mapped to HTTP 500 Internal Server Error because this is a server-side
 * configuration issue (e.g., missing template file, blank template body).
 * </p>
 */
public class EmptyTemplateException extends EmailSendingException{

    public EmptyTemplateException(String message) {super(message);}

    public EmptyTemplateException(String message, Throwable cause) {super(message, cause);}
}