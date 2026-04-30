package cires.bemodule.dtos2;

import cires.bemodule.annotations.PasswordMatches;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@PasswordMatches
@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ResetPasswordRequest {

    private String token;

    private String newPassword;

    private String confirmPassword;

}
