package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.PermissionDTO;
import cires.bemodule.services.PermissionService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@AllArgsConstructor
public class PermissionController {

    private final PermissionService permissionService;

    @GetMapping
    @PreAuthorize("hasAuthority('role:read')")
    public List<PermissionDTO> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
}