package cires.bemodule.dtos;

import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class ParticipantDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String registrationSource;

}
