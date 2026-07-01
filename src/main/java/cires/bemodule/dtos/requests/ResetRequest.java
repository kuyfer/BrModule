package cires.bemodule.dtos.requests;

import cires.bemodule.utilities.EmailNormalizer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request to initiate a password reset (sends email with token).
 */

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ResetRequest {

    @Email(message = "Please enter a valid email address")
    @NotBlank(message = "Email is required")
    @JsonDeserialize(using = EmailNormalizer.class)
    private String email;

}