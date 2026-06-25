package cires.bemodule.exceptions.controllerexceptions;

public class PermissionNotFoundException extends RuntimeException {
    public PermissionNotFoundException(Long id) {super("Permission not found with id: " + id);}
}
