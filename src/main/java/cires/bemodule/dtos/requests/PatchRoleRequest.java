package cires.bemodule.dtos.requests;

import cires.bemodule.enums.RoleType;
import lombok.Data;

import java.util.Set;

@Data
public class PatchRoleRequest {
    private RoleType roleName;  
    private Set<Long> permissionIds;
}