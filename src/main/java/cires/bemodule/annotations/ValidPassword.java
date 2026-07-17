package cires.bemodule.annotations;

import cires.bemodule.validators.PasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Field‑ or parameter‑level validation annotation that ensures a password
 * meets the required strength criteria.
 * <p>
 * The password must:
 * <ul>
 *   <li>be 8 to 20 characters long</li>
 *   <li>contain at least one digit</li>
 *   <li>contain at least one lowercase letter</li>
 *   <li>contain at least one uppercase letter</li>
 *   <li>contain no whitespace</li>
 * </ul>
 * </p>
 *
 * <h3>Usage example</h3>
 * <pre>{@code
 * public class RegistrationRequest {
 *     @ValidPassword
 *     private String password;
 *     // …
 * }
 * }</pre>
 *
 * @see PasswordValidator
 */
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface ValidPassword {

    /**
     * Error message returned when the password does not satisfy the strength rules.
     *
     * @return default error message describing the password policy
     */
    String message() default "Invalid password. Must be 8-20 chars, include uppercase, lowercase, digit, no spaces.";

    /**
     * Validation groups.
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