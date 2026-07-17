package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.CreateRoleRequest;
import cires.bemodule.dtos.requests.PatchRoleRequest;
import cires.bemodule.dtos.views.RoleDTO;
import cires.bemodule.services.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/roles")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasAuthority('role:create')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public ResponseEntity<Page<RoleDTO>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<RoleDTO> patch(@PathVariable Long id,
                                         @Valid @RequestBody PatchRoleRequest request) {
        return ResponseEntity.ok(roleService.patchRole(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('role:delete')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<RoleDTO> addPermissions(@PathVariable Long id,
                                                  @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(id, permissionIds));
    }

    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasAuthority('role:update')")
    public ResponseEntity<RoleDTO> removePermissions(@PathVariable Long id,
                                                     @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(id, permissionIds));
    }
}