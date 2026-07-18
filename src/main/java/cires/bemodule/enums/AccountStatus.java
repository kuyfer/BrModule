package cires.bemodule.enums;

/**
 * Represents the activation state of a user account.
 * <p>
 * New accounts are created in {@code PENDING} status (e.g. waiting for password setup)
 * and become {@code ACTIVE} once confirmed. Accounts can be deactivated to
 * {@code INACTIVE} to prevent login without deleting the user data.
 * </p>
 */
public enum AccountStatus {

    /** The account is fully active and the user can log in. */
    ACTIVE,

    /** The account is deactivated (the user cannot log in). */
    INACTIVE,

    /** The account has been created but not yet activated (e.g. awaiting password setup). */
    PENDING
}