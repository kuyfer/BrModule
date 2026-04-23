package cires.bemodule.exceptions.controllerexceptions;

public class ParticipantNotFoundException extends RuntimeException{

        public ParticipantNotFoundException(Long id) {super("Participant not found with id: " + id);}
}
