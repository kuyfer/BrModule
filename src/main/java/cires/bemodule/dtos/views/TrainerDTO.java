package cires.bemodule.dtos.views;

import cires.bemodule.entities.Organization;
import lombok.*;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TrainerDTO {
    private Long id;
    private String specialty;
    private UserDTO user;
    private Set<Organization> affiliatedOrganizations;
}



