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
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody CreateRoleRequest request) {
        return ResponseEntity.ok(roleService.createRole(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATIONAL_ADMIN')")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findRoleById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'OPERATIONAL_ADMIN')")
    public ResponseEntity<Page<RoleDTO>> getAll(@PageableDefault(size = 20) Pageable pageable) {
        return ResponseEntity.ok(roleService.findAll(pageable));
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleDTO> patch(@PathVariable Long id,
                                         @Valid @RequestBody PatchRoleRequest request) {
        return ResponseEntity.ok(roleService.patchRole(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleDTO> addPermissions(@PathVariable Long id,
                                                  @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.addPermissionsToRole(id, permissionIds));
    }

    @DeleteMapping("/{id}/permissions")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RoleDTO> removePermissions(@PathVariable Long id,
                                                     @RequestBody Set<Long> permissionIds) {
        return ResponseEntity.ok(roleService.removePermissionsFromRole(id, permissionIds));
    }
}