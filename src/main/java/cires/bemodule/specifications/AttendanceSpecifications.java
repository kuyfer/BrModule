package cires.bemodule.specifications;

import cires.bemodule.entities.Attendance;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

/**
 * Utility class providing reusable {@link Specification} building blocks for
 * querying {@link Attendance} entities with dynamic filters.
 * <p>
 * These private helper methods are designed to be combined (via
 * {@link Specification#and(Specification)} etc.) inside public factory methods
 * to construct type‑safe queries without writing JPQL.
 * </p>
 * <p>
 * The class cannot be instantiated – it only contains static helpers.
 * </p>
 *
 * @see Attendance
 */
public class AttendanceSpecifications {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if instantiation is attempted
     */
    private AttendanceSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a specification that matches {@link Attendance} records where
     * the value of a given entity field equals a supplied value.
     * <p>
     * The {@code field} argument supports nested property paths using the dot
     * notation (e.g., {@code "session.id"}).  The path is traversed
     * dynamically at query time.
     * </p>
     * <p>
     * If {@code value} is {@code null}, the specification returns {@code null},
     * which is treated by Spring Data JPA as a no‑op (the predicate is
     * omitted).
     * </p>
     *
     * @param field the name of the entity attribute, optionally with nested
     *              paths separated by dots (e.g., "session.id")
     * @param value the value to compare; may be {@code null} to disable the
     *              filter
     * @return a specification that performs an equality comparison, or
     *         {@code null} if the value is {@code null}
     */
    private static Specification<Attendance> equalTo(String field, Object value) {
        return (root, q, cb) -> {
            if (value == null) return null;
            String[] parts = field.split("\\.");
            var path = root.get(parts[0]);
            for (int i = 1; i < parts.length; i++) path = path.get(parts[i]);
            return cb.equal(path, value);
        };
    }

    /**
     * Creates a specification that filters attendance records with a
     * {@code date} greater than or equal to the given {@code startTime}.
     * <p>
     * When {@code startTime} is {@code null}, the specification returns
     * {@code null} (the predicate is not applied).
     * </p>
     *
     * @param startTime the inclusive lower bound for the attendance date; may
     *                  be {@code null}
     * @return a specification for the lower date bound, or {@code null} if
     *         {@code startTime} is {@code null}
     */
    private static Specification<Attendance> dateFrom(LocalDate startTime) {
        return (root, q, cb) ->
                startTime == null ? null : cb.greaterThanOrEqualTo(root.get("date"), startTime);
    }

    /**
     * Creates a specification that filters attendance records with a
     * {@code date} less than or equal to the given {@code date}.
     * <p>
     * If {@code date} is {@code null}, the specification returns {@code null}
     * (the filter is disabled).
     * </p>
     *
     * @param date the inclusive upper bound for the attendance date; may be
     *             {@code null}
     * @return a specification for the upper date bound, or {@code null} if
     *         {@code date} is {@code null}
     */
    private static Specification<Attendance> dateTo(LocalDate date) {
        return (root, q, cb) ->
                date == null ? null : cb.lessThanOrEqualTo(root.get("date"), date);
    }
}