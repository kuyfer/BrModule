package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Request to create a new role with optional initial permissions.
 */
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateRoleRequest {
    @NotNull(message = "Role type is required")
    private String roleName;

    private Set<Long> permissionIds;
}