package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class PatchParticipantRequest {

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    private String phoneNumber;

    private String address;

    private String registrationSource;

}
