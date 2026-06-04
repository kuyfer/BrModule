package cires.bemodule.dtos.requests;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.annotations.ValidPassword;
import cires.bemodule.interfaces.PasswordConfirmable;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatches
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ResetPasswordRequest implements PasswordConfirmable {

    @NotBlank(message = "Token is required")
    private String token;

    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String passwordConfirm;

}
