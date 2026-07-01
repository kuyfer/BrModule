package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.Permission} was not found.
 */
public class PermissionNotFoundException extends EntityNotFoundException {

    public PermissionNotFoundException(Long id) {super("Permission not found with id: " + id);}

    public PermissionNotFoundException(String message) { super(message); }

    public PermissionNotFoundException(String message, Throwable cause) { super(message, cause); }

}