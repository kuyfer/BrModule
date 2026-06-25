package cires.bemodule.dtos.requests;

import cires.bemodule.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class CreateRoleRequest {
    @NotNull(message = "Role type is required")
    private RoleType roleName;

    private Set<Long> permissionIds;
}
