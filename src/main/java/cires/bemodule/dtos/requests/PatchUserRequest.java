package cires.bemodule.dtos.requests;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class PatchUserRequest {
    private String username;


    private String password;

    private String firstName;


    private String lastName;

    private String email;
}
