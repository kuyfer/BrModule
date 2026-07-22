package cires.bemodule.specifications;

import cires.bemodule.entities.Trainer;
import org.springframework.data.jpa.domain.Specification;

/**
 * Utility class providing static {@link Specification} factories for
 * querying {@link Trainer} entities with dynamic filters.
 * <p>
 * These specifications are designed to be used with
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * to build type‑safe, composable queries without writing JPQL.
 * </p>
 * <p>
 * The class is not meant to be instantiated – it only contains static helpers.
 * </p>
 *
 * @see Trainer
 * @see org.springframework.data.jpa.domain.Specification
 */
public class TrainerSpecifications {

    /**
     * Private constructor that prevents instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if called (including via reflection)
     */
    private TrainerSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a {@link Specification} that filters trainers by their
     * {@code speciality} field.
     * <p>
     * If the provided {@code speciality} is {@code null}, the specification
     * becomes a no‑op (a conjunction that matches all trainers).
     * </p>
     *
     * @param speciality the exact speciality string to filter on (e.g., "Java");
     *                   may be {@code null} to disable filtering
     * @return a specification that matches trainers with the given speciality
     */
    public static Specification<Trainer> hasSpeciality(String speciality) {
        return (root, query, criteriaBuilder) ->
                speciality == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("speciality"), speciality);
    }
}