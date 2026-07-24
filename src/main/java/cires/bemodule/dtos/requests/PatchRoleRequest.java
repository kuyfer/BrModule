package cires.bemodule.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Request to partially update a role (name and/or permissions).
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchRoleRequest {
    private String roleName;
    private Set<Long> permissionIds;
}