package cires.bemodule.interfaces;

/**
 * Contract for objects that expose a password field and a password confirmation field.
 * <p>
 * Implement this interface on any DTO or form class that requires password matching
 * validation.  The {@link cires.bemodule.annotations.PasswordMatches} annotation
 * works together with this interface to ensure the two password fields are equal.
 * </p>
 *
 * @see cires.bemodule.annotations.PasswordMatches
 * @see cires.bemodule.validators.PasswordMatchesValidator
 */
public interface PasswordConfirmable {

    /**
     * Returns the password entered by the user.
     *
     * @return the plain‑text password, may be {@code null}
     */
    String getPassword();

    /**
     * Returns the password confirmation (re‑typed password) entered by the user.
     *
     * @return the plain‑text confirmation password, may be {@code null}
     */
    String getPasswordConfirm();
}