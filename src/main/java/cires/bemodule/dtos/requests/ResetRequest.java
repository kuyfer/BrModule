package cires.bemodule.dtos.requests;

import cires.bemodule.utilities.EmailNormalizer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ResetRequest {

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    @JsonDeserialize(using = EmailNormalizer.class)
    private String email;

}
