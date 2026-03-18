package cires.bemodule.dtos;

import cires.bemodule.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

// TODO : add roles and status
@Data @AllArgsConstructor @NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private AccountStatus accountStatus;
    private Collection<RoleDTO> roles;

}
