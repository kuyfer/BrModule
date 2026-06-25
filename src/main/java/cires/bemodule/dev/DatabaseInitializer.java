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
        // ─── Define all permissions ──────────────────────────────────────────
        List<Permission> allPermissions = List.of(
                // User
                createPermission("user:create", "user", "create"),
                createPermission("user:read", "user", "read"),
                createPermission("user:update", "user", "update"),
                createPermission("user:delete", "user", "delete"),

                // Role
                createPermission("role:create", "role", "create"),
                createPermission("role:read", "role", "read"),
                createPermission("role:update", "role", "update"),
                createPermission("role:delete", "role", "delete"),

                // Session
                createPermission("session:create", "session", "create"),
                createPermission("session:read", "session", "read"),
                createPermission("session:update", "session", "update"),
                createPermission("session:delete", "session", "delete"),

                // Report
                createPermission("report:view", "report", "view"),
                createPermission("report:export", "report", "export"),
                createPermission("report:create", "report", "create"),
                createPermission("report:delete", "report", "delete"),

                // Trainer
                createPermission("trainer:create", "trainer", "create"),
                createPermission("trainer:read", "trainer", "read"),
                createPermission("trainer:update", "trainer", "update"),
                createPermission("trainer:delete", "trainer", "delete"),
                createPermission("trainer:assign", "trainer", "assign"),

                // Subsidiary
                createPermission("subsidiary:create", "subsidiary", "create"),
                createPermission("subsidiary:read", "subsidiary", "read"),
                createPermission("subsidiary:update", "subsidiary", "update"),
                createPermission("subsidiary:delete", "subsidiary", "delete"),

                // Organization
                createPermission("organization:create", "organization", "create"),
                createPermission("organization:read", "organization", "read"),
                createPermission("organization:update", "organization", "update"),
                createPermission("organization:delete", "organization", "delete"),

                // Participant
                createPermission("participant:create", "participant", "create"),
                createPermission("participant:read", "participant", "read"),
                createPermission("participant:update", "participant", "update"),
                createPermission("participant:delete", "participant", "delete"),

                // Bulk import
                createPermission("import:execute", "import", "execute"),

                // ─── Attendance (NEW) ────────────────────────────────────────────
                createPermission("attendance:mark", "attendance", "mark"),
                createPermission("attendance:validate", "attendance", "validate"),
                createPermission("attendance:correct", "attendance", "correct"),
                createPermission("attendance:read", "attendance", "read"),

                // Notification
                createPermission("notification:read", "notification", "read")
        );

        // ─── Save all permissions if they don't exist ──────────────────────
        for (Permission perm : allPermissions) {
            if (!permissionRepository.existsByName(perm.getName())) {
                permissionRepository.save(perm);
            }
        }

        // ─── Retrieve all permission objects for assignment ────────────────
        Permission userCreate = permissionRepository.findByName("user:create");
        Permission userRead = permissionRepository.findByName("user:read");
        Permission userUpdate = permissionRepository.findByName("user:update");
        Permission userDelete = permissionRepository.findByName("user:delete");

        Permission roleCreate = permissionRepository.findByName("role:create");
        Permission roleRead = permissionRepository.findByName("role:read");
        Permission roleUpdate = permissionRepository.findByName("role:update");
        Permission roleDelete = permissionRepository.findByName("role:delete");

        Permission sessionCreate = permissionRepository.findByName("session:create");
        Permission sessionRead = permissionRepository.findByName("session:read");
        Permission sessionUpdate = permissionRepository.findByName("session:update");
        Permission sessionDelete = permissionRepository.findByName("session:delete");

        Permission reportView = permissionRepository.findByName("report:view");
        Permission reportExport = permissionRepository.findByName("report:export");
        Permission reportCreate = permissionRepository.findByName("report:create");
        Permission reportDelete = permissionRepository.findByName("report:delete");

        Permission trainerCreate = permissionRepository.findByName("trainer:create");
        Permission trainerRead = permissionRepository.findByName("trainer:read");
        Permission trainerUpdate = permissionRepository.findByName("trainer:update");
        Permission trainerDelete = permissionRepository.findByName("trainer:delete");
        Permission trainerAssign = permissionRepository.findByName("trainer:assign");

        Permission subsidiaryCreate = permissionRepository.findByName("subsidiary:create");
        Permission subsidiaryRead = permissionRepository.findByName("subsidiary:read");
        Permission subsidiaryUpdate = permissionRepository.findByName("subsidiary:update");
        Permission subsidiaryDelete = permissionRepository.findByName("subsidiary:delete");

        Permission organizationCreate = permissionRepository.findByName("organization:create");
        Permission organizationRead = permissionRepository.findByName("organization:read");
        Permission organizationUpdate = permissionRepository.findByName("organization:update");
        Permission organizationDelete = permissionRepository.findByName("organization:delete");

        Permission participantCreate = permissionRepository.findByName("participant:create");
        Permission participantRead = permissionRepository.findByName("participant:read");
        Permission participantUpdate = permissionRepository.findByName("participant:update");
        Permission participantDelete = permissionRepository.findByName("participant:delete");

        Permission importExecute = permissionRepository.findByName("import:execute");

        // ─── Attendance permissions ──────────────────────────────────────────
        Permission attendanceMark = permissionRepository.findByName("attendance:mark");
        Permission attendanceValidate = permissionRepository.findByName("attendance:validate");
        Permission attendanceCorrect = permissionRepository.findByName("attendance:correct");
        Permission attendanceRead = permissionRepository.findByName("attendance:read");

        // Notification permissions
        Permission notificationRead = permissionRepository.findByName("notification:read");

        // ─── Define permissions per role ────────────────────────────────────
        Map<RoleType, Set<Permission>> rolePermissions = Map.of(
                // SUPER_ADMIN – everything
                RoleType.SUPER_ADMIN,
                Set.of(
                        userCreate, userRead, userUpdate, userDelete,
                        roleCreate, roleRead, roleUpdate, roleDelete,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate, reportDelete,
                        trainerCreate, trainerRead, trainerUpdate, trainerDelete, trainerAssign,
                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
                        participantCreate, participantRead, participantUpdate, participantDelete,
                        importExecute,
                        // Attendance
                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead , notificationRead
                ),

                // OPERATIONAL_ADMIN – full attendance rights too
                RoleType.OPERATIONAL_ADMIN,
                Set.of(
                        userRead, userUpdate,
                        roleRead,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate, reportDelete,
                        trainerCreate, trainerRead, trainerUpdate, trainerAssign,
                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
                        participantRead, participantUpdate,
                        importExecute,
                        // Attendance – all
                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead , notificationRead
                ),

                // TRAINING_MANAGER – can mark, validate, read; but not correct
                RoleType.TRAINING_MANAGER,
                Set.of(
                        userRead,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate,
                        trainerRead, trainerAssign,
                        participantCreate, participantRead, participantUpdate,
                        subsidiaryRead,
                        organizationRead,
                        // Attendance – mark, validate, read (no correct)
                        attendanceMark, attendanceValidate, attendanceRead, notificationRead
                ),

                // TRAINER – can mark, validate, read; no correct
                RoleType.TRAINER,
                Set.of(
                        userRead,
                        sessionRead, sessionUpdate,
                        reportView,
                        participantRead,
                        // Attendance – mark, validate, read (no correct)
                        attendanceMark, attendanceValidate, attendanceRead
                ),

                // READ_ONLY – can only read attendance
                RoleType.READ_ONLY,
                Set.of(
                        userRead,
                        sessionRead,
                        reportView,
                        trainerRead,
                        subsidiaryRead,
                        organizationRead,
                        participantRead,
                        // Attendance – read only
                        attendanceRead,
                        notificationRead
                )
        );

        // ─── Create roles if they don't exist ──────────────────────────────
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