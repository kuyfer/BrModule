package cires.bemodule.dtos2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class RegisterResponse {

    private Long userId;
    private String userName;
    private String message;

}
