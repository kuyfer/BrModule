package cires.bemodule.specifications;

import cires.bemodule.entities.Role;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<User> hasRole(String roleName) {
        return (root, query, criteriaBuilder) -> {
            if (roleName == null || roleName.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            Join<User, Role> rolesJoin = root.join("roles");
            return criteriaBuilder.equal(rolesJoin.get("roleName"), roleName);
        };
    }

    public static Specification<User> hasStatus(AccountStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("accountStatus"), status);
        };
    }

}