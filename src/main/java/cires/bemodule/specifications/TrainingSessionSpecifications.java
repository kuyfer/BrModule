package cires.bemodule.specifications;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class providing static {@link Specification} factories for
 * building dynamic, type‑safe queries on {@link TrainingSession} entities.
 * <p>
 * These specifications are typically used with
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * to filter sessions by mode or status without writing JPQL or native queries.
 * </p>
 * <p>
 * The class cannot be instantiated – it contains only static helper methods.
 * </p>
 *
 * @see TrainingSession
 * @see TrainingSessionMode
 * @see TrainingSessionStatus
 */
public class TrainingSessionSpecifications {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private TrainingSessionSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a {@link Specification} that filters sessions by their
     * {@link TrainingSessionMode}.
     * <p>
     * If the provided {@code mode} is {@code null}, the specification returns
     * a conjunction (matches all sessions) – effectively disabling the filter.
     * </p>
     *
     * @param mode the training session mode to filter by (e.g.,
     *             {@link TrainingSessionMode#ON_SITE}); may be {@code null}
     * @return a specification for the given mode
     */
    public static Specification<TrainingSession> hasMode(TrainingSessionMode mode) {
        return (root, query, criteriaBuilder) ->
                mode == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("mode"), mode);
    }

    /**
     * Creates a {@link Specification} that filters sessions by their
     * {@link TrainingSessionStatus}.
     * <p>
     * When {@code status} is {@code null}, the filter is disabled and all
     * sessions are matched.
     * </p>
     *
     * @param status the training session status to filter by (e.g.,
     *               {@link TrainingSessionStatus#ONGOING}); may be {@code null}
     * @return a specification for the given status
     */
    public static Specification<TrainingSession> hasStatus(TrainingSessionStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("status"), status);
    }

    // TODO: add trainer id specification
}