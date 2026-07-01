package cires.bemodule.exceptions.notfound;

/**
 * Indicates that an {@link cires.bemodule.entities.Organization} was not found.
 */
public class OrganizationNotFoundException extends EntityNotFoundException{
    public OrganizationNotFoundException(Long id) {super("Organization not found with id: " + id);}

    public OrganizationNotFoundException(String name) {super("Organization not found with name: " + name);}

    public OrganizationNotFoundException(String message, Throwable cause) { super(message, cause); }
}