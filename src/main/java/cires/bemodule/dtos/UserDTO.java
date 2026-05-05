package cires.bemodule.dtos;

import cires.bemodule.enums.AccountStatus;
import lombok.*;

import java.util.Collection;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class UserDTO {

    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private AccountStatus accountStatus;
    private Collection<RoleDTO> roles;

}
