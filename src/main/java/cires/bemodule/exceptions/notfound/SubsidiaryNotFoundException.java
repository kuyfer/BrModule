package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.Subsidiary} was not found.
 */
public class SubsidiaryNotFoundException extends EntityNotFoundException {

    public SubsidiaryNotFoundException(Long id) {
        super("Subsidiary not found with id: " + id);
    }

    public SubsidiaryNotFoundException(String name) {
        super("Subsidiary not found with name: " + name);
    }

    public SubsidiaryNotFoundException(String message, Throwable cause) { super(message, cause); }
}