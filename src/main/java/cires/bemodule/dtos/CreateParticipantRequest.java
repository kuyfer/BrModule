package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class CreateParticipantRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String address;
    private String registrationSource;
}
