package cires.bemodule.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class RegisterResponse {

    public Long userId;
    public String userName;
    public String message;

}
