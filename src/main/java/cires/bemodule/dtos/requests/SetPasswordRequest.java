package cires.bemodule.dtos.requests;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.interfaces.PasswordConfirmable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordMatches
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class SetPasswordRequest implements PasswordConfirmable {
    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String passwordConfirm;
}