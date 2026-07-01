package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.TrainingSession} was not found.
 */
public class TrainingSessionNotFoundException extends EntityNotFoundException{

        public TrainingSessionNotFoundException(Long id) {super("Training session not found with id: " + id);}

        public TrainingSessionNotFoundException(String message) { super(message); }

        public TrainingSessionNotFoundException(String message, Throwable cause) { super(message, cause); }
}