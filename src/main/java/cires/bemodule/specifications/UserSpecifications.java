package cires.bemodule.specifications;

import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> isAdmin() {
        return (root, query, cb) -> {
            Join<User, Role> rolesJoin = root.join("roles");
            return cb.equal(rolesJoin.get("roleName"), "SUPER_ADMIN");
        };
    }

    public static Specification<User> isActive() {
        return (root, query, cb) -> cb.equal(root.get("isActive"), true);
    }
}
