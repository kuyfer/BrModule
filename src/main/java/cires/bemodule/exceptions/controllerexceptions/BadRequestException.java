package cires.bemodule.exceptions.controllerexceptions;

public class BadRequestException extends RuntimeException{

        public BadRequestException(String message) {
        super(message);
    }
}
