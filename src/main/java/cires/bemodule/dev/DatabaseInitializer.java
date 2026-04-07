package cires.bemodule.dev;
import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
import cires.bemodule.enums.RoleType;
import cires.bemodule.repositories.PermissionRepository;
import cires.bemodule.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (roleRepository.count() == 0) {
            initializePermissionsAndRoles();
        }
    }

    private void initializePermissionsAndRoles() {
        List<Permission> allPermissions = List.of(
                createPermission("user:create", "user", "create"),
                createPermission("user:read", "user", "read"),
                createPermission("user:update", "user", "update"),
                createPermission("user:delete", "user", "delete"),
                createPermission("role:manage", "role", "manage"),
                createPermission("session:create", "session", "create"),
                createPermission("session:read", "session", "read"),
                createPermission("session:update", "session", "update"),
                createPermission("session:delete", "session", "delete"),
                createPermission("report:view", "report", "view"),
                createPermission("trainer:assign", "trainer", "assign")
        );

        for (Permission perm : allPermissions) {
            if (!permissionRepository.existsByName(perm.getName())) {
                permissionRepository.save(perm);
            }
        }


        Permission userCreate = permissionRepository.findByName("user:create");
        Permission userRead = permissionRepository.findByName("user:read");
        Permission userUpdate = permissionRepository.findByName("user:update");
        Permission userDelete = permissionRepository.findByName("user:delete");
        Permission roleManage = permissionRepository.findByName("role:manage");
        Permission sessionCreate = permissionRepository.findByName("session:create");
        Permission sessionRead = permissionRepository.findByName("session:read");
        Permission sessionUpdate = permissionRepository.findByName("session:update");
        Permission sessionDelete = permissionRepository.findByName("session:delete");
        Permission reportView = permissionRepository.findByName("report:view");
        Permission trainerAssign = permissionRepository.findByName("trainer:assign");

        Map<RoleType, Set<Permission>> rolePermissions = Map.of(
                RoleType.SUPER_ADMIN,
                Set.of(userCreate, userRead, userUpdate, userDelete, roleManage,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, trainerAssign),

                RoleType.OPERATIONAL_ADMIN,
                Set.of(userRead, userUpdate, sessionCreate, sessionRead, sessionUpdate,
                        sessionDelete, reportView, trainerAssign),

                RoleType.TRAINING_MANAGER,
                Set.of(sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, trainerAssign, userRead),

                RoleType.TRAINER,
                Set.of(sessionRead, userRead, sessionUpdate),

                RoleType.READ_ONLY,
                Set.of(sessionRead, userRead, reportView)
        );

        for (Map.Entry<RoleType, Set<Permission>> entry : rolePermissions.entrySet()) {
            RoleType roleType = entry.getKey();
            if (!roleRepository.existsByRoleName(roleType)) {
                Role role = new Role();
                role.setRoleName(roleType);
                role.setPermissions(entry.getValue());
                roleRepository.save(role);
                System.out.println("Created role: " + roleType);
            }
        }
    }

    private Permission createPermission(String name, String resource, String action) {
        Permission perm = new Permission();
        perm.setName(name);
        perm.setResource(resource);
        perm.setAction(action);
        return perm;
    }
}