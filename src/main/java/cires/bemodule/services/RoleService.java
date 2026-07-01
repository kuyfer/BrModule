package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateRoleRequest;
import cires.bemodule.dtos.requests.PatchRoleRequest;
import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.entities.Permission;
import cires.bemodule.entities.Role;
import cires.bemodule.enums.RoleType;
import cires.bemodule.exceptions.notfound.PermissionNotFoundException;
import cires.bemodule.exceptions.notfound.RoleNotFoundException;
import cires.bemodule.exceptions.business.ConflictException;
import cires.bemodule.mappers.RoleMapper;
import cires.bemodule.repositories.PermissionRepository;
import cires.bemodule.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;

    // ################################# CREATE ######################################

    @Transactional
    public RoleDTO createRole(CreateRoleRequest request) {
        // Check uniqueness
        if (roleRepository.existsByRoleName(request.getRoleName())) {
            throw new ConflictException("Role with name '" + request.getRoleName() + "' already exists.");
        }

        Role role = roleMapper.toEntity(request);

        // Assign permissions if provided
        if (request.getPermissionIds() != null && !request.getPermissionIds().isEmpty()) {
            Set<Permission> permissions = resolvePermissions(request.getPermissionIds());
            role.setPermissions(permissions);
        }

        Role saved = roleRepository.save(role);
        return roleMapper.toDto(saved);
    }

    // ################################# READ ########################################

    public RoleDTO findRoleById(Long id) {
        Role role = getRoleOrThrow(id);
        return roleMapper.toDto(role);
    }

    public RoleDTO findRoleByName(RoleType roleName) {
        Role role = roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new RoleNotFoundException(roleName));
        return roleMapper.toDto(role);
    }

    public Page<RoleDTO> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable)
                .map(roleMapper::toDto);
    }

    // ################################# UPDATE ######################################

    @Transactional
    public RoleDTO patchRole(Long id, PatchRoleRequest request) {
        Role existing = getRoleOrThrow(id);

        // Update name if provided and changed
        if (request.getRoleName() != null && request.getRoleName() != existing.getRoleName()) {
            if (roleRepository.existsByRoleName(request.getRoleName())) {
                throw new ConflictException("Role with name '" + request.getRoleName() + "' already exists.");
            }
            existing.setRoleName(request.getRoleName());
        }

        // Update permissions if provided (replace entire set)
        if (request.getPermissionIds() != null) {
            Set<Permission> permissions = resolvePermissions(request.getPermissionIds());
            existing.setPermissions(permissions);
        }

        Role updated = roleRepository.save(existing);
        return roleMapper.toDto(updated);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteRole(Long id) {
        Role role = getRoleOrThrow(id);
        // Optional: prevent deletion of system roles (e.g., SUPER_ADMIN)
        // if (role.getRoleName() == RoleType.SUPER_ADMIN) {
        //     throw new ConflictException("Cannot delete the SUPER_ADMIN role.");
        // }
        roleRepository.delete(role);
    }

    // ################################# PERMISSION MANAGEMENT ########################

    @Transactional
    public RoleDTO addPermissionsToRole(Long roleId, Set<Long> permissionIds) {
        Role role = getRoleOrThrow(roleId);
        Set<Permission> permissions = resolvePermissions(permissionIds);
        role.getPermissions().addAll(permissions);
        Role updated = roleRepository.save(role);
        return roleMapper.toDto(updated);
    }

    @Transactional
    public RoleDTO removePermissionsFromRole(Long roleId, Set<Long> permissionIds) {
        Role role = getRoleOrThrow(roleId);
        Set<Permission> permissionsToRemove = resolvePermissions(permissionIds);
        role.getPermissions().removeAll(permissionsToRemove);
        Role updated = roleRepository.save(role);
        return roleMapper.toDto(updated);
    }

    // ################################# UTILS ######################################

    private Role getRoleOrThrow(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException(id));
    }

    private Set<Permission> resolvePermissions(Set<Long> permissionIds) {
        if (permissionIds == null || permissionIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Permission> permissions = new HashSet<>();
        for (Long id : permissionIds) {
            Permission perm = permissionRepository.findById(id)
                    .orElseThrow(() -> new PermissionNotFoundException(id));
            permissions.add(perm);
        }
        return permissions;
    }
}