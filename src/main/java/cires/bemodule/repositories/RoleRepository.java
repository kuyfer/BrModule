package cires.bemodule.repositories;

import cires.bemodule.entities.Role;
import cires.bemodule.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Override
    Optional<Role> findById(Long aLong);

    Optional<Role> findByroleName(RoleType roleName);

    boolean existsByRoleName(RoleType roleName);

}
