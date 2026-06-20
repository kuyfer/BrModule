package cires.bemodule.exceptions.controllerexceptions;

import cires.bemodule.exceptions.validationexceptions.ConflictException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import cires.bemodule.exceptions.securityexceptions.SecurityException;

import java.net.URI;
import java.time.Instant;
import java.util.Arrays;
import java.util.Optional;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({
                UserNotFoundException.class,
                TrainerNotFoundException.class,
                TrainingSessionNotFoundException.class,
                ParticipantNotFoundException.class,
                NotificationNotFoundException.class,
                RoleNotFoundException.class})
    public ProblemDetail handleNotFound(RuntimeException exception, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, "not-found", exception.getMessage(), request);
    }

    @ExceptionHandler(SecurityException.class)
    public ProblemDetail handleSecurityException(SecurityException ex, HttpServletRequest request) {
        String slug = ex.getErrorCode().toLowerCase().replace("_", "-");

        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());
        problem.setType(URI.create("/errors/" + slug));
        problem.setTitle(ex.getStatus().getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());
        problem.setProperty("errorCode", ex.getErrorCode());
        return problem;
    }

    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequestHandler(BadRequestException exception, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST, "bad-request", exception.getMessage(), request);
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail conflictExceptionHandler(ConflictException exception, HttpServletRequest request){
        return build(HttpStatus.CONFLICT, "conflict", exception.getMessage(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = handleValidationException(ex);
        return ResponseEntity.status(status.value()).body(problemDetail);
    }

    private ProblemDetail build(HttpStatus status, String errorSlug,
                                                String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create("/errors/" + errorSlug));
        problem.setTitle(status.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    private ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String details = getErrorsDetails(ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), details);
        problem.setType(URI.create("/errors/bad-request"));
        problem.setTitle("Bad request");
        problem.setInstance(ex.getBody().getInstance());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException(JwtException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "invalid-token", ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "internal-error", "An unexpected error occurred", request);
    }

    // idk how it works but it works
    // alteratively use Binding Results and Field Errors
    // source: https://medium.com/@RoussiAbdelghani/error-handling-in-spring-web-using-rfc-9457-specification-f2cc8398e285
    private String getErrorsDetails(MethodArgumentNotValidException ex) {
        return Optional.of(ex.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                        .filter(msg -> !ObjectUtils.isEmpty(msg))
                        .reduce("Please make sure to provide a valid request, ", (a, b) -> a + " " + b)
                )
                .orElse("").toString();
    }
}
