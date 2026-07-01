package cires.bemodule.dtos.responses;

import lombok.*;

/**
 * Response after creating a new participant.
 */
@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class CreateParticipantResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private String message;

}