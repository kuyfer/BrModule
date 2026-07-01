package cires.bemodule.exceptions.notfound;

/**
 * Indicates that an {@link cires.bemodule.entities.Attendance} record was not found.
 */
public class AttendanceNotFoundException extends EntityNotFoundException {

    public AttendanceNotFoundException(Long id) {super("Attendance not found with id: " + id);}

    public AttendanceNotFoundException(String message) { super(message); }

    public AttendanceNotFoundException(String message, Throwable cause) { super(message, cause); }
}