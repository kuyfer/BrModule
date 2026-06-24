package cires.bemodule.exceptions.controllerexceptions;

public class SubsidiaryNotFoundException extends RuntimeException {
    public SubsidiaryNotFoundException(Long id) {
        super("Subsidiary not found with id: " + id);
    }
    public SubsidiaryNotFoundException(String name) {
        super("Subsidiary not found with name: " + name);
    }
}
