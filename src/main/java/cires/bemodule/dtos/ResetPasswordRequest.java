package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @Getter @AllArgsConstructor @NoArgsConstructor
public class ResetPasswordRequest {

    private String token;
    private String newPassword;

}
