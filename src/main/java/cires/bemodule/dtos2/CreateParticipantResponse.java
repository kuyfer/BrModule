package cires.bemodule.dtos2;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CreateParticipantResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String message;

}
