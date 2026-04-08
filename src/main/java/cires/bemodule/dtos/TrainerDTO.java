package cires.bemodule.dtos;

import cires.bemodule.entities.Organization;
import cires.bemodule.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor
public class TrainerDTO {

    private Long id;
    private String specialty;
    private User user;
    private Set<Organization> affiliatedOrganizations;

}


