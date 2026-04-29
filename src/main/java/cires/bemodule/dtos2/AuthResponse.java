package cires.bemodule.dtos2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// MAYBE : add a constructor with 3 parameters (bearer)
@Data @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType ;
    private Long expiresIn;

}
