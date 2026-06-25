package cires.bemodule.dtos.views;

import cires.bemodule.enums.RoleType;
import lombok.*;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RoleDTO {

    private Long id;
    private RoleType roleName;
    private Set<PermissionDTO> permissions;

}
