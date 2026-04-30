package cires.bemodule.dtos2;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class LoginRequest {
    
    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 50)
    private String username;
    
    @NotBlank(message = "Password is required")
    private String password;

}
