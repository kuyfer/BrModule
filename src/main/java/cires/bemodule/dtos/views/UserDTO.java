package cires.bemodule.dtos.views;

import cires.bemodule.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
