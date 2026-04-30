package cires.bemodule.dtos2;

import cires.bemodule.annotations.PasswordMatches;
import cires.bemodule.annotations.ValidPassword;
import cires.bemodule.interfaces.PasswordConfirmable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@PasswordMatches
@Data @AllArgsConstructor @NoArgsConstructor
public class RegisterRequest implements PasswordConfirmable {
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @NotBlank(message = "Password is required")
    @ValidPassword
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String passwordConfirm;

    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

}
