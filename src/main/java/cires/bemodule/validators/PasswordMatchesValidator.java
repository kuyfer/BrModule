package cires.bemodule.validators;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.interfaces.PasswordConfirmable;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validates that the {@code password} and {@code passwordConfirm} fields of a
 * {@link PasswordConfirmable} object match.
 * <p>
 * This validator is triggered by the {@link PasswordMatches} annotation and
 * returns {@code true} only when both fields are non‑{@code null} and equal.
 * </p>
 *
 * @see PasswordMatches
 */
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    /**
     * Checks whether the password fields match.
     *
     * @param pass    the object containing the password and confirmation; must not be {@code null}
     * @param context validation context (not modified by this validator)
     * @return {@code true} if both password fields are non‑{@code null} and equal;
     *         {@code false} otherwise
     */
    @Override
    public boolean isValid(PasswordConfirmable pass, ConstraintValidatorContext context) {
        return pass.getPassword() != null &&
                pass.getPassword().equals(pass.getPasswordConfirm());
    }
}