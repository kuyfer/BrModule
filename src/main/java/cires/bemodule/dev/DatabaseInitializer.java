package cires.bemodule.dev;

import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
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
                // ... (all your permission definitions unchanged)
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
        Permission reportRead = permissionRepository.findByName("report:read");

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

// Attendance permissions
        Permission attendanceMark = permissionRepository.findByName("attendance:mark");
        Permission attendanceValidate = permissionRepository.findByName("attendance:validate");
        Permission attendanceCorrect = permissionRepository.findByName("attendance:correct");
        Permission attendanceRead = permissionRepository.findByName("attendance:read");

// Notification permissions
        Permission notificationRead = permissionRepository.findByName("notification:read");

// Dashboard permissions
        Permission dashboardExecutive = permissionRepository.findByName("dashboard:executive");
        Permission dashboardOperational = permissionRepository.findByName("dashboard:operational");
        Permission dashboardTrainer = permissionRepository.findByName("dashboard:trainer");
        Permission dashboardAudit = permissionRepository.findByName("dashboard:audit");

// Extra permissions
        Permission permissionRead = permissionRepository.findByName("permission:read");
        Permission auditRead = permissionRepository.findByName("audit:read");

        // ─── Define permissions per role ────────────────────────────────────
// Keys are the predefined role names as plain strings.
        Map<String, Set<Permission>> rolePermissions = Map.of(
                "SUPER_ADMIN", Set.of(
                        userCreate, userRead, userUpdate, userDelete,
                        roleCreate, roleRead, roleUpdate, roleDelete,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate, reportDelete, reportRead,
                        trainerCreate, trainerRead, trainerUpdate, trainerDelete, trainerAssign,
                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
                        participantCreate, participantRead, participantUpdate, participantDelete,
                        importExecute,
                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead,
                        notificationRead,
                        dashboardExecutive, dashboardOperational, dashboardTrainer, dashboardAudit,
                        permissionRead,
                        auditRead
                ),
                "OPERATIONAL_ADMIN", Set.of(
                        userRead, userUpdate,
                        roleRead,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate, reportDelete, reportRead,
                        trainerCreate, trainerRead, trainerUpdate, trainerAssign,
                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
                        participantRead, participantUpdate,
                        importExecute,
                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead,
                        notificationRead,
                        dashboardOperational,
                        permissionRead,
                        auditRead
                ),
                "TRAINING_MANAGER", Set.of(
                        userRead,
                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
                        reportView, reportExport, reportCreate, reportRead,
                        trainerRead, trainerAssign,
                        participantCreate, participantRead, participantUpdate,
                        subsidiaryRead,
                        organizationRead,
                        attendanceMark, attendanceValidate, attendanceRead,
                        notificationRead,
                        dashboardTrainer,
                        permissionRead
                ),
                "TRAINER", Set.of(
                        userRead,
                        sessionRead, sessionUpdate,
                        reportView, reportRead,
                        participantRead,
                        attendanceMark, attendanceValidate, attendanceRead,
                        dashboardTrainer
                ),
                "READ_ONLY", Set.of(
                        userRead,
                        sessionRead,
                        reportView, reportRead,
                        trainerRead,
                        subsidiaryRead,
                        organizationRead,
                        participantRead,
                        attendanceRead,
                        notificationRead
                )
        );

// ─── Create roles if they don't exist ──────────────────────────────
        for (Map.Entry<String, Set<Permission>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role();
                role.setRoleName(roleName);
                role.setPermissions(entry.getValue());
                roleRepository.save(role);
                System.out.println("Created role: " + roleName);
            }
        }

        // ─── Create roles if they don't exist ──────────────────────────────
        for (Map.Entry<String, Set<Permission>> entry : rolePermissions.entrySet()) {
            String roleName = entry.getKey();
            if (!roleRepository.existsByRoleName(roleName)) {
                Role role = new Role();
                role.setRoleName(roleName);               // now a plain string
                role.setPermissions(entry.getValue());
                roleRepository.save(role);
                System.out.println("Created role: " + roleName);
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