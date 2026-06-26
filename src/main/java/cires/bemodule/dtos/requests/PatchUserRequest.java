package cires.bemodule.dtos.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchUserRequest {

    @Size(min = 2, max = 50)
    private String username;

    @Size(min = 2, max = 50)
    private String firstName;

    @Size(min = 2, max = 50)
    private String lastName;

    @Email(message = "Invalid email format")
    @Size(min = 2, max = 50)
    private String email;
}