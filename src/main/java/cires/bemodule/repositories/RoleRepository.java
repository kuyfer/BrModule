package cires.bemodule.repositories;

import cires.bemodule.entities.Role;
import cires.bemodule.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(RoleType roleName);

    Optional<Role> findByRoleName(RoleType roleName);
}