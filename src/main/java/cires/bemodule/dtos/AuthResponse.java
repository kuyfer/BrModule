package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
// MAYBE : add a constructor with 3 parameters (bearer)
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String tokenType ;
    private Long expiresIn;

}
