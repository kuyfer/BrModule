package cires.bemodule.repositories;

import cires.bemodule.entities.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Override
    Optional<Permission> findById(Long aLong);

    Permission findByName(String name);

    Optional<Permission> findByAction(String action);
    boolean existsByName(String name);


}
