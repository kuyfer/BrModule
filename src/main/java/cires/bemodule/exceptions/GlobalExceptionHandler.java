package cires.bemodule.exceptions;

import cires.bemodule.exceptions.business.BadRequestException;
import cires.bemodule.exceptions.email.EmailSendingException;
import cires.bemodule.exceptions.email.InvalidEmailAddressException;
import cires.bemodule.exceptions.imports.FileProcessingException;
import cires.bemodule.exceptions.business.ImportException;
import cires.bemodule.exceptions.imports.ImportValidationException;
import cires.bemodule.exceptions.notfound.*;
import cires.bemodule.exceptions.business.ConflictException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import cires.bemodule.exceptions.security.SecurityException;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Global exception handler for the application.
 * <p>
 * Converts exceptions into RFC 9457 {@link ProblemDetail} responses,
 * providing consistent error payloads across all REST endpoints.
 * </p>
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // ─── Not Found (404) ────────────────────────────────────────────────────────

    /**
     * Handles all {@link EntityNotFoundException} subtypes.
     * <p>
     * Returns HTTP 404 Not Found when a requested entity does not exist.
     * </p>
     *
     * @param exception the thrown exception
     * @param request   the current HTTP request
     * @return a 404 error detail
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleNotFound(EntityNotFoundException exception, HttpServletRequest request){
        return build(HttpStatus.NOT_FOUND, "not-found", exception.getMessage(), request);
    }

    // ─── Bad Request / Conflict (400, 409) ────────────────────────────────────

    /**
     * Handles {@link BadRequestException}.
     * <p>
     * Returns HTTP 400 Bad Request when the request syntax or parameters are invalid.
     * </p>
     *
     * @param exception the thrown exception
     * @param request   the current HTTP request
     * @return a 400 error detail
     */
    @ExceptionHandler(BadRequestException.class)
    public ProblemDetail badRequestHandler(BadRequestException exception, HttpServletRequest request){
        return build(HttpStatus.BAD_REQUEST, "bad-request", exception.getMessage(), request);
    }

    /**
     * Handles {@link ConflictException} and its subtypes.
     * <p>
     * Returns HTTP 409 Conflict when a request conflicts with the current state
     * of the resource (e.g., duplicate email / username).
     * </p>
     *
     * @param exception the thrown exception
     * @param request   the current HTTP request
     * @return a 409 error detail
     */
    @ExceptionHandler(ConflictException.class)
    public ProblemDetail conflictExceptionHandler(ConflictException exception, HttpServletRequest request){
        return build(HttpStatus.CONFLICT, "conflict", exception.getMessage(), request);
    }

    // ─── Import Errors (422) ─────────────────────────────────────────────

    /**
     * Handles {@link ImportValidationException}.
     * <p>
     * Returns HTTP 422 Unprocessable Entity when the import file has structural
     * issues (e.g., missing headers, invalid format).
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 422 error detail
     */
    @ExceptionHandler(ImportValidationException.class)
    public ProblemDetail handleImportValidationException(ImportValidationException ex, HttpServletRequest request) {
        return build(HttpStatus.UNPROCESSABLE_CONTENT, "import-validation-failed", ex.getMessage(), request);
    }

    /**
     * Handles {@link FileProcessingException}.
     * <p>
     * Returns HTTP 422 Unprocessable Entity when the import file is corrupt,
     * malformed, or cannot be parsed (e.g., invalid Excel structure).
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 422 error detail
     */
    @ExceptionHandler(FileProcessingException.class)
    public ProblemDetail handleFileProcessingException(FileProcessingException ex, HttpServletRequest request) {
        return build(HttpStatus.UNPROCESSABLE_CONTENT, "file-processing-failed", ex.getMessage(), request);
    }

    /**
     * Fallback handler for any other {@link ImportException} not caught by specific handlers.
     * <p>
     * Returns HTTP 422 Unprocessable Entity.
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 422 error detail
     */
    @ExceptionHandler(ImportException.class)
    public ProblemDetail handleImportException(ImportException ex, HttpServletRequest request) {
        return build(HttpStatus.UNPROCESSABLE_CONTENT, "import-failed", ex.getMessage(), request);
    }

    // ─── Security (403, 401) ───────────────────────────────────────────────────

    /**
     * Handles {@link SecurityException} and its subtypes.
     * <p>
     * Returns the dynamic HTTP status and error code defined in the exception
     * (typically 401 Unauthorized or 403 Forbidden).
     * </p>
     *
     * @param ex      the thrown security exception
     * @param request the current HTTP request
     * @return a problem detail with the appropriate security status
     */
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

    /**
     * Handles {@link JwtException} thrown by the JWT processing layer.
     * <p>
     * Returns HTTP 400 Bad Request when a JWT is malformed, expired, or otherwise invalid.
     * </p>
     *
     * @param ex      the thrown JWT exception
     * @param request the current HTTP request
     * @return a 400 error detail
     */
    @ExceptionHandler(JwtException.class)
    public ProblemDetail handleJwtException(JwtException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "invalid-token", ex.getMessage(), request);
    }

    // ─── Email Errors (400, 500) ──────────────────────────────────────────────

    /**
     * Handles {@link InvalidEmailAddressException}.
     * <p>
     * Returns HTTP 400 Bad Request when the provided email address is malformed.
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 400 error detail
     */
    @ExceptionHandler(InvalidEmailAddressException.class)
    public ProblemDetail handleInvalidEmailAddress(InvalidEmailAddressException ex, HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, "invalid-email-address", ex.getMessage(), request);
    }

    /**
     * Handles {@link EmailSendingException} and its other subtypes.
     * <p>
     * Returns HTTP 500 Internal Server Error for server-side email failures.
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 500 error detail
     */
    @ExceptionHandler(EmailSendingException.class)
    public ProblemDetail handleEmailSendingError(EmailSendingException ex, HttpServletRequest request) {
        // For SmtpTimeoutException and EmptyTemplateException, this is the fallback.
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "email-sending-failed", ex.getMessage(), request);
    }

    // ─── Validation (MethodArgumentNotValid) ──────────────────────────────────

    /**
     * Overrides Spring's default handling for {@link MethodArgumentNotValidException}.
     * <p>
     * This is triggered by validation failures on {@code @Valid} request bodies or parameters.
     * Returns HTTP 400 Bad Request with detailed validation messages.
     * </p>
     *
     * @param ex      the validation exception
     * @param headers the HTTP headers
     * @param status  the HTTP status (400)
     * @param request the current web request
     * @return a 400 error response with validation details
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ProblemDetail problemDetail = handleValidationException(ex);
        return ResponseEntity.status(status.value()).body(problemDetail);
    }

    /**
     * Builds a validation error response from {@link MethodArgumentNotValidException}.
     *
     * @param ex the validation exception
     * @return a populated {@link ProblemDetail} for validation errors
     */
    private ProblemDetail handleValidationException(MethodArgumentNotValidException ex) {
        String details = getErrorsDetails(ex);
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(ex.getStatusCode(), details);
        problem.setType(URI.create("/errors/bad-request"));
        problem.setTitle("Bad request");
        problem.setInstance(ex.getBody().getInstance());
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }

    /**
     * Extracts and concatenates error messages from a validation exception.
     * <p>
     * Source inspiration: Medium article on RFC 9457 error handling.
     * </p>
     *
     * @param ex the validation exception
     * @return a single formatted string containing all validation errors
     * @see <a href="https://medium.com/@RoussiAbdelghani/error-handling-in-spring-web-using-rfc-9457-specification-f2cc8398e285">Reference</a>
     */
    private String getErrorsDetails(MethodArgumentNotValidException ex) {
        return Optional.of(ex.getDetailMessageArguments())
                .map(args -> Arrays.stream(args)
                        .filter(msg -> !ObjectUtils.isEmpty(msg))
                        .reduce("Please make sure to provide a valid request, ", (a, b) -> a + " " + b)
                )
                .orElse("").toString();
    }

    // ─── Fallback (500) – catch-all for any unhandled exception ──────────

    /**
     * Fallback handler for any exception not explicitly handled above.
     * <p>
     * Returns HTTP 500 Internal Server Error with a generic message.
     * The original exception is logged server‑side for debugging.
     * </p>
     *
     * @param ex      the thrown exception
     * @param request the current HTTP request
     * @return a 500 problem detail
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleAllUnhandledExceptions(Exception ex, HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "internal-server-error",
                "An unexpected error occurred. Please contact support.",
                request);
    }

    // ─── Private Helpers ──────────────────────────────────────────────────────

    /**
     * Builds a standardized {@link ProblemDetail} response.
     *
     * @param status   the HTTP status to return
     * @param errorSlug     the error type slug (used as the URI path suffix)
     * @param detail   the human-readable error message
     * @param request  the current HTTP request (used for the instance URI)
     * @return a fully populated {@link ProblemDetail} object
     */
    private ProblemDetail build(HttpStatus status, String errorSlug,
                                String detail, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create("/errors/" + errorSlug));
        problem.setTitle(status.getReasonPhrase());
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", Instant.now());

        return problem;
    }
}