package cires.bemodule.exceptions.notfound;

import cires.bemodule.enums.RoleType;

/**
 * Indicates that a {@link cires.bemodule.entities.Role} was not found.
 */
public class RoleNotFoundException extends EntityNotFoundException {

    public RoleNotFoundException() {
        super("Role not found");
    }

    public RoleNotFoundException(Long id) {
        super("Role not found with id: " + id);
    }

    public RoleNotFoundException(RoleType roleName) {
        super("Role not found with name: " + roleName);
    }

    public RoleNotFoundException(String message, Throwable cause) { super(message, cause); }
}