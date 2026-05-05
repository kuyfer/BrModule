package cires.bemodule.dtos;

import cires.bemodule.entities.Organization;
import cires.bemodule.entities.User;
import lombok.*;

import java.util.Set;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TrainerDTO {

    private Long id;
    private String specialty;
    private User user;
    private Set<Organization> affiliatedOrganizations;

}


