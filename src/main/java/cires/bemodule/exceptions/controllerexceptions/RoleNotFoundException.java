package cires.bemodule.exceptions.controllerexceptions;

public class RoleNotFoundException extends RuntimeException {

    public RoleNotFoundException() {
        super("Role not found");
    }

}
