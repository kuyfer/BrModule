//package cires.bemodule.dev;
//
//import cires.bemodule.entities.Permission;
//import cires.bemodule.entities.Role;
//import cires.bemodule.enums.RoleType;
//import cires.bemodule.repositories.PermissionRepository;
//import cires.bemodule.repositories.RoleRepository;
//import cires.bemodule.repositories.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class DatabaseInitializer implements CommandLineRunner {
//
//    private final RoleRepository roleRepository;
//    private final PermissionRepository permissionRepository;
//    private final UserRepository userRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    @Transactional
//    public void run(String... args) {
//        if (roleRepository.count() == 0) {
//            initializePermissionsAndRoles();
//        }
//    }
//
//    private void initializePermissionsAndRoles() {
//        // ─── Define all permissions ──────────────────────────────────────────
//        List<Permission> allPermissions = List.of(
//                // ... (all your permission definitions unchanged)
//        );
//
//        // ─── Save all permissions if they don't exist ──────────────────────
//        for (Permission perm : allPermissions) {
//            if (!permissionRepository.existsByName(perm.getName())) {
//                permissionRepository.save(perm);
//            }
//        }
//
//        // ─── Define permissions per role ────────────────────────────────────
//// Keys are the predefined role names as plain strings.
//        Map<String, Set<Permission>> rolePermissions = Map.of(
//                "SUPER_ADMIN", Set.of(
//                        userCreate, userRead, userUpdate, userDelete,
//                        roleCreate, roleRead, roleUpdate, roleDelete,
//                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
//                        reportView, reportExport, reportCreate, reportDelete, reportRead,
//                        trainerCreate, trainerRead, trainerUpdate, trainerDelete, trainerAssign,
//                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
//                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
//                        participantCreate, participantRead, participantUpdate, participantDelete,
//                        importExecute,
//                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead,
//                        notificationRead,
//                        dashboardExecutive, dashboardOperational, dashboardTrainer, dashboardAudit,
//                        permissionRead,
//                        auditRead
//                ),
//                "OPERATIONAL_ADMIN", Set.of(
//                        userRead, userUpdate,
//                        roleRead,
//                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
//                        reportView, reportExport, reportCreate, reportDelete, reportRead,
//                        trainerCreate, trainerRead, trainerUpdate, trainerAssign,
//                        subsidiaryCreate, subsidiaryRead, subsidiaryUpdate, subsidiaryDelete,
//                        organizationCreate, organizationRead, organizationUpdate, organizationDelete,
//                        participantRead, participantUpdate,
//                        importExecute,
//                        attendanceMark, attendanceValidate, attendanceCorrect, attendanceRead,
//                        notificationRead,
//                        dashboardOperational,
//                        permissionRead,
//                        auditRead
//                ),
//                "TRAINING_MANAGER", Set.of(
//                        userRead,
//                        sessionCreate, sessionRead, sessionUpdate, sessionDelete,
//                        reportView, reportExport, reportCreate, reportRead,
//                        trainerRead, trainerAssign,
//                        participantCreate, participantRead, participantUpdate,
//                        subsidiaryRead,
//                        organizationRead,
//                        attendanceMark, attendanceValidate, attendanceRead,
//                        notificationRead,
//                        dashboardTrainer,
//                        permissionRead
//                ),
//                "TRAINER", Set.of(
//                        userRead,
//                        sessionRead, sessionUpdate,
//                        reportView, reportRead,
//                        participantRead,
//                        attendanceMark, attendanceValidate, attendanceRead,
//                        dashboardTrainer
//                ),
//                "READ_ONLY", Set.of(
//                        userRead,
//                        sessionRead,
//                        reportView, reportRead,
//                        trainerRead,
//                        subsidiaryRead,
//                        organizationRead,
//                        participantRead,
//                        attendanceRead,
//                        notificationRead
//                )
//        );
//
//// ─── Create roles if they don't exist ──────────────────────────────
//        for (Map.Entry<String, Set<Permission>> entry : rolePermissions.entrySet()) {
//            String roleName = entry.getKey();
//            if (!roleRepository.existsByRoleName(roleName)) {
//                Role role = new Role();
//                role.setRoleName(roleName);
//                role.setPermissions(entry.getValue());
//                roleRepository.save(role);
//                System.out.println("Created role: " + roleName);
//            }
//        }
//
//        // ─── Create roles if they don't exist ──────────────────────────────
//        for (Map.Entry<String, Set<Permission>> entry : rolePermissions.entrySet()) {
//            String roleName = entry.getKey();
//            if (!roleRepository.existsByRoleName(roleName)) {
//                Role role = new Role();
//                role.setRoleName(roleName);               // now a plain string
//                role.setPermissions(entry.getValue());
//                roleRepository.save(role);
//                System.out.println("Created role: " + roleName);
//            }
//        }
//    }
//
//    private Permission createPermission(String name, String resource, String action) {
//        Permission perm = new Permission();
//        perm.setName(name);
//        perm.setResource(resource);
//        perm.setAction(action);
//        return perm;
//    }
//}