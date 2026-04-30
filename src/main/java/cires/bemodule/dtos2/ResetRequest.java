package cires.bemodule.dtos2;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ResetRequest {

    @Email(message = "Please enter a valid email address")
    private String email;

}
