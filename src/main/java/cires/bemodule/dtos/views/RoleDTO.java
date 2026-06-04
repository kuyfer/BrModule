package cires.bemodule.dtos.views;

import cires.bemodule.enums.RoleType;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class RoleDTO {

    private Long id;
    private RoleType roleName;

}
