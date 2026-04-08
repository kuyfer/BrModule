package cires.bemodule.exceptions;

public class TrainingSessionNotFoundException extends RuntimeException{

        public TrainingSessionNotFoundException(Long id) {super("Training session not found with id: " + id);}
}
