package cires.bemodule.dtos;

import cires.bemodule.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class RoleDTO {

    private Long id;
    private RoleType roleName;

}
