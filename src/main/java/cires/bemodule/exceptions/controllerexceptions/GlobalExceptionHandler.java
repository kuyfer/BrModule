package cires.bemodule.exceptions.controllerexceptions;

import cires.bemodule.exceptions.validationexceptions.ConflictException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorEntity> badRequestHandler(BadRequestException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorEntity> userNotFoundHandler(UserNotFoundException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);

    }

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ErrorEntity> trainerNotFoundHandler(TrainerNotFoundException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorEntity> runtimeExceptionHandler(RuntimeException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.FORBIDDEN.value())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);

    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorEntity> conflictExceptionHamdler(ConflictException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.CONFLICT.value())
                .build();
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(TrainingSessionNotFoundException.class)
    public ResponseEntity<ErrorEntity> trainingSesssionNotFoundHandler(TrainingSessionNotFoundException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ParticipantNotFoundException.class)
    public ResponseEntity<ErrorEntity> participantNOtFoundHandler(ParticipantNotFoundException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<ErrorEntity> notificationNotFoundHandler(NotificationNotFoundException exception){
        ErrorEntity error = ErrorEntity.builder()
                .timeStamp(LocalDateTime.now())
                .message(exception.getMessage())
                .httpStatus(HttpStatus.NOT_FOUND.value())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException exception){
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation error");
        return ResponseEntity.badRequest().body(errorMessage);
    }

}
