package cires.bemodule.exceptions.securityexceptions;

import org.springframework.http.HttpStatus;

public class InvalidJwtTokenException extends SecurityException {

    public InvalidJwtTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }

    public InvalidJwtTokenException(String message, Throwable cause) {
        super(message, cause, HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }

}
