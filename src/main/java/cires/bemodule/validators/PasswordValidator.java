package cires.bemodule.validators;

import cires.bemodule.annotations.ValidPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

/**
 * Validates that a password matches the required strength policy.
 * <p>
 * The policy is:
 * <ul>
 *   <li>Length: 8 to 20 characters</li>
 *   <li>At least one digit</li>
 *   <li>At least one lowercase letter</li>
 *   <li>At least one uppercase letter</li>
 *   <li>No whitespace characters</li>
 * </ul>
 * </p>
 *
 * @see ValidPassword
 */
public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    private static final String PASS_PATTERN =
            "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,20}$";
    private final Pattern pattern = Pattern.compile(PASS_PATTERN);

    /**
     * Validates the password against the pattern.
     *
     * @param password the password string to validate; may be {@code null}
     * @param context  validation context (not used)
     * @return {@code true} if the password matches the policy, {@code false} otherwise
     */
    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) return false;
        return pattern.matcher(password).matches();
    }
}