package cires.bemodule.dtos.responses;

import lombok.*;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CreateParticipantResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String message;

}
