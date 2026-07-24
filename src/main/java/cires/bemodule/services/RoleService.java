package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateRoleRequest;
import cires.bemodule.dtos.requests.PatchRoleRequest;
import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
import cires.bemodule.exceptions.notfound.PermissionNotFoundException;
import cires.bemodule.exceptions.notfound.RoleNotFoundException;
import cires.bemodule.exceptions.business.ConflictException;
import cires.bemodule.mappers.RoleMapper;
import cires.bemodule.repositories.PermissionRepository;
import cires.bemodule.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    // ################################# CREATE ######################################

    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            log.warn("Attempt to create role with duplicate name: {}", request.getRoleName());
            throw new ConflictException("Role with name '" + request.getRoleName() + "' already exists.");
        }

        Role role = roleMapper.toRole(request);

        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = resolvePermissions(request.getPermissionIds());
            role.setPermissions(permissions);
        }

        Role saved = roleRepository.save(role);
        log.info("Role created successfully with id: {} and name: {}", saved.getId(), saved.getRoleName());
        return roleMapper.toRoleDto(saved);
    }

    // ################################# READ ########################################

    public RoleDTO findRoleById(Long id) {
        log.debug("Finding role by id: {}", id);
        Role role = getRoleOrThrow(id);
        return roleMapper.toRoleDto(role);
    }

    public RoleDTO findRoleByName(String roleName) {
        log.debug("Finding role by name: {}", roleName);
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    log.warn("Role not found with name: {}", roleName);
                    return new RoleNotFoundException(roleName);
                });
        return roleMapper.toRoleDto(role);
    }

    public Page<RoleDTO> findAll(Pageable pageable) {
        log.debug("Fetching all roles, pageable: {}", pageable);
        Page<RoleDTO> page = roleRepository.findAll(pageable)
                .map(roleMapper::toRoleDto);
        log.debug("Found {} roles (page {} of {})", page.getNumberOfElements(), page.getNumber(), page.getTotalPages());
        return page;
    }

    // ################################# UPDATE ######################################

    public RoleDTO patchRole(Long id, PatchRoleRequest request) {
        log.info("Patching User id={} with request: {}", id, request);
        Role role = getRoleOrThrow(id);
        roleMapper.patchRoleFromRequest(request, role);
        Role saved = roleRepository.save(role);
        log.info("User patched id={}", saved.getId());
        return roleMapper.toRoleDto(saved);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteRole(Long id) {
        log.info("Deleting role with id: {}", id);
        Role role = getRoleOrThrow(id);

        log.debug("Removing role references from users for role id: {}", id);
        roleRepository.deleteRoleReferences(id);

        roleRepository.delete(role);
        log.info("Role deleted successfully with id: {}", id);
    }
    // ################################# PERMISSION MANAGEMENT ########################

    @Transactional
    public RoleDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        log.info("Adding permissions {} to role id: {}", permissionIds, roleId);
        Role role = getRoleOrThrow(roleId);
        Set<Permission> permissions = resolvePermissions(permissionIds);
        role.getPermissions().addAll(permissions);
        Role updated = roleRepository.save(role);
        log.info("Permissions added successfully to role id: {}", roleId);
        return roleMapper.toRoleDto(updated);
    }

    @Transactional
    public RoleDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        log.info("Removing permissions {} from role id: {}", permissionIds, roleId);
        Role role = getRoleOrThrow(roleId);
        Set<Permission> permissionsToRemove = resolvePermissions(permissionIds);
        role.getPermissions().removeAll(permissionsToRemove);
        Role updated = roleRepository.save(role);
        log.info("Permissions removed successfully from role id: {}", roleId);
        return roleMapper.toRoleDto(updated);
    }

    // ################################# UTILS ######################################

    private Role getRoleOrThrow(Long id) {
        log.debug("Looking up role by id: {}", id);
        return roleRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Role not found with id: {}", id);
                    return new RoleNotFoundException(id);
                });
    }

    private Set<Permission> resolvePermissions(Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Permission> permissions = new HashSet<>();
        for (Long id : permissionIds) {
            Permission perm = permissionRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("Permission not found with id: {}", id);
                        return new PermissionNotFoundException(id);
                    });
            permissions.add(perm);
        }
        return permissions;
    }
}