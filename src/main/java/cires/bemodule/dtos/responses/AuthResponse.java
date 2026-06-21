package cires.bemodule.dtos.responses;

import lombok.*;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class AuthResponse {

    private String accessToken;

    private String refreshToken;

    private String tokenType ;

    private Long expiresIn;

}
