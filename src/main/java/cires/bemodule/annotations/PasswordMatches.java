package cires.bemodule.annotations;

import cires.bemodule.interfaces.PasswordConfirmable;
import cires.bemodule.validators.PasswordMatchesValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Class‑level validation annotation that checks whether the password
 * and password confirmation fields of a {@link PasswordConfirmable}
 * object are equal.
 * <p>
 * Apply this annotation to any DTO or form object that implements
 * {@link PasswordConfirmable} to enforce that the user typed the
 * same password twice.
 * </p>
 *
 * <h3>Usage example</h3>
 * <pre>{@code
 * @PasswordMatches
 * public class RegistrationRequest implements PasswordConfirmable {
 *     private String password;
 *     private String passwordConfirm;
 *     // getters and setters …
 * }
 * }</pre>
 *
 * @see PasswordMatchesValidator
 */
@Target(TYPE)
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {

    /**
     * Error message that is returned when the two passwords do not match.
     *
     * @return the default error message "Passwords do not match"
     */
    String message() default "Passwords do not match";

    /**
     * Validation groups for grouped validation.
     *
     * @return empty array by default
     */
    Class<?>[] groups() default {};

    /**
     * Payload for extensibility (e.g., severity levels).
     *
     * @return empty array by default
     */
    Class<? extends Payload>[] payload() default {};
}