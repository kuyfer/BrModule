package cires.bemodule.repositories;

import cires.bemodule.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByRoleName(String roleName);

    Optional<Role> findByRoleName(String roleName);

    @Modifying
    @Query(value = "DELETE FROM users_roles WHERE roles_id = :roleId", nativeQuery = true)
    void deleteRoleReferences(@Param("roleId") Long roleId);
}