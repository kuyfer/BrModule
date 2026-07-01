package cires.bemodule.exceptions.notfound;

/**
 * Indicates that a {@link cires.bemodule.entities.Trainer} was not found.
 */
public class TrainerNotFoundException extends EntityNotFoundException{

        public TrainerNotFoundException(Long id) {
        super("Trainer not found with id: " + id);
    }

        public TrainerNotFoundException(String message) { super(message); }

        public TrainerNotFoundException(String message, Throwable cause) { super(message, cause); }
}