package cires.bemodule.exceptions.controllerexceptions;

import cires.bemodule.enums.RoleType;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Role not found");
    }
    public RoleNotFoundException(Long id) {
        super("Role not found with id: " + id);
    }
    public RoleNotFoundException(RoleType roleName) {
        super("Role not found with name: " + roleName);
    }
}
