package cires.bemodule.enums;

/**
 * Represents the lifecycle status of a {@link cires.bemodule.entities.TrainingSession}.
 * <p>
 * The statuses progress in a logical order, though not all transitions are allowed:
 * <ul>
 *   <li>{@link #DRAFT} → {@link #SCHEDULED} (when published)</li>
 *   <li>{@link #SCHEDULED} → {@link #ONGOING} (when the session starts)</li>
 *   <li>{@link #ONGOING} → {@link #COMPLETED} (when the session ends)</li>
 *   <li>{@link #SCHEDULED} or {@link #ONGOING} → {@link #CANCELLED} (if cancelled)</li>
 *   <li>{@link #SCHEDULED} or {@link #ONGOING} → {@link #POSTPONED} (if postponed)</li>
 * </ul>
 */
public enum TrainingSessionStatus {

    /** Session is being created but not yet published. */
    DRAFT,

    /** Session has been published and is scheduled to start. */
    SCHEDULED,

    /** Session is currently in progress. */
    ONGOING,

    /** Session has finished successfully. */
    COMPLETED,

    /** Session has been cancelled and will not take place. */
    CANCELLED,

    /** Session has been postponed to a later date. */
    POSTPONED
}