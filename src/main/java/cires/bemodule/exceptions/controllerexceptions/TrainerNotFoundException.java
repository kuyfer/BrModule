package cires.bemodule.exceptions.controllerexceptions;

public class TrainerNotFoundException extends RuntimeException{

        public TrainerNotFoundException(Long id) {
        super("Trainer not found with id: " + id);
    }
}
