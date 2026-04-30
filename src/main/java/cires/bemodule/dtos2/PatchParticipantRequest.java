package cires.bemodule.dtos2;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PatchParticipantRequest {

    private String firstName;

    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    private String address;

    private String registrationSource;

}
