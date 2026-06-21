package cires.bemodule.dtos.responses;

import lombok.*;

@Getter @Builder @AllArgsConstructor @NoArgsConstructor
public class RegisterResponse {

    private Long userId;

    private String userName;

    private String message;

}
