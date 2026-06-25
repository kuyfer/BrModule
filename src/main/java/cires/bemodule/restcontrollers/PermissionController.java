package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.entities.Permission;
import cires.bemodule.repositories.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {

    private final PermissionRepository permissionRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('role:manage')")
    public List<PermissionDTO> getAllPermissions() {
        return permissionRepository.findAll().stream()
                .map(this::toSummary)
                .toList();
    }

    private PermissionDTO toSummary(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .resource(permission.getResource())
                .action(permission.getAction())
                .build();
    }
}