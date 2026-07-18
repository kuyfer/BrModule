package cires.bemodule.enums;

/**
 * Pre‑defined roles in the application.
 * <p>
 * Each role corresponds to a specific set of permissions and access levels.
 * These roles are seeded by {@code DatabaseInitializer} and are typically
 * not created or deleted at runtime.
 * </p>
 */
public enum RoleType {

    /** Full system access – can manage users, roles, sessions, and all data. */
    SUPER_ADMIN,

    /** Operational administrator – manages sessions, attendance, trainers, and subsidiaries. */
    OPERATIONAL_ADMIN,

    /** Training manager – creates and manages sessions, participants, and reports. */
    TRAINING_MANAGER,

    /** Trainer – marks attendance, validates days, and views assigned sessions. */
    TRAINER,

    /** Read‑only access – can view sessions, participants, and reports but cannot modify anything. */
    READ_ONLY
}