package cires.bemodule.exceptions;

public class ParticipantNotFoundException extends RuntimeException{

        public ParticipantNotFoundException(Long id) {super("Participant not found with id: " + id);}
}
