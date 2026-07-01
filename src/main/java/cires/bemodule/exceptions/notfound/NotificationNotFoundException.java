package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.Notification} was not found.
 */
public class NotificationNotFoundException extends EntityNotFoundException{

    public NotificationNotFoundException(Long id) {super("Notification not found with id: " + id);}

    public NotificationNotFoundException(String message) { super(message); }

    public NotificationNotFoundException(String message, Throwable cause) { super(message, cause); }
}