package cires.bemodule.dtos.views;

import lombok.*;

import java.util.Set;

/**
 * View projection for roles with their permissions.
 */

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RoleDTO {
    private Long id;
    private String roleName;
    private Set<PermissionDTO> permissions;
}