package cires.bemodule.dtos.views;

import cires.bemodule.enums.RegistrationSource;
import lombok.*;

/**
 * View projection for participants.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ParticipantDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private RegistrationSource registrationSource;
}