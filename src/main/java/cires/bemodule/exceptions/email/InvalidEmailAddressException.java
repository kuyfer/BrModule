package cires.bemodule.exceptions.email;

/**
 * Indicates that a recipient email address is malformed or invalid.
 * <p>
 * Mapped to HTTP 400 Bad Request because this is a client-side input error.
 * </p>
 */
public class InvalidEmailAddressException extends EmailSendingException{

    public InvalidEmailAddressException(String message) {super(message);}

    public InvalidEmailAddressException(String message, Throwable cause) {super(message, cause);}
}