package cires.bemodule.specifications;

import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

/**
 * A utility class providing static {@link Specification} factories for
 * querying {@link User} entities with dynamic filters.
 * <p>
 * These specifications are typically used in combination with
 * {@link org.springframework.data.jpa.repository.JpaSpecificationExecutor}
 * to build type‑safe, programmatic queries without writing JPQL or native SQL.
 * </p>
 * <p>
 * The class cannot be instantiated – it only contains static helper methods.
 * </p>
 *
 * @see org.springframework.data.jpa.domain.Specification
 * @see User
 * @see Role
 */
public class UserSpecifications {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * @throws UnsupportedOperationException if reflection attempts to instantiate
     */
    private UserSpecifications() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Creates a {@link Specification} that filters users who have been assigned
     * a {@link Role} with the given {@code roleName}.
     * <p>
     * If the provided {@code roleName} is {@code null} or empty, the specification
     * will match all users (i.e., a conjunction that does not affect the query).
     * </p>
     *
     * @param roleName the exact name of the role to filter by (e.g., {@code "TRAINER"});
     *                 may be {@code null} or empty to disable the filter
     * @return a specification that matches users with the specified role
     */
    public static Specification<User> hasRole(String roleName) {
        return (root, query, criteriaBuilder) -> {
            if (roleName == null || roleName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<User, Role> rolesJoin = root.join("roles");
            return criteriaBuilder.equal(rolesJoin.get("roleName"), roleName);
        };
    }

    /**
     * Creates a {@link Specification} that filters users based on their
     * {@link AccountStatus}.
     * <p>
     * When {@code status} is {@code null}, the specification is effectively
     * ignored and all users are returned.
     * </p>
     *
     * @param status the account status to match (e.g., {@code ACTIVE}); may be
     *               {@code null} to disable the filter
     * @return a specification that matches users with the given status
     */
    public static Specification<User> hasStatus(AccountStatus status) {
        return (root, query, criteriaBuilder) ->
                status == null ? criteriaBuilder.conjunction() : criteriaBuilder.equal(root.get("accountStatus"), status);
    }
}