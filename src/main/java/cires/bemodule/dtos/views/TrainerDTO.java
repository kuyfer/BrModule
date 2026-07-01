package cires.bemodule.dtos.views;

import lombok.*;

import java.util.Set;

/**
 * View projection for trainers.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TrainerDTO {
    private Long id;
    private String speciality;
    private UserDTO user;
    private Set<OrganizationDTO> affiliatedOrganizations;
}