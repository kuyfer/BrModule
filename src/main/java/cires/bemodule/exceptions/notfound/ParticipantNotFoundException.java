package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.Participant} was not found.
 */
public class ParticipantNotFoundException extends EntityNotFoundException{

        public ParticipantNotFoundException(Long id) {super("Participant not found with id: " + id);}

        public ParticipantNotFoundException(String message) { super(message); }

        public ParticipantNotFoundException(String message, Throwable cause) { super(message, cause); }
}