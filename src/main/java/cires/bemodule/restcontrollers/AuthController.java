package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.LoginRequest;
import cires.bemodule.dtos.requests.RefreshTokenRequest;
import cires.bemodule.dtos.requests.RegisterRequest;
import cires.bemodule.dtos.responses.AuthResponse;
import cires.bemodule.dtos.responses.RegisterResponse;
import cires.bemodule.entities.User;
import cires.bemodule.security.services.AuthService;
import cires.bemodule.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
            User user = userService.registerUser(registerRequest);
            RegisterResponse response = new RegisterResponse(user.getId(), user.getUsername(), "Login successful!");
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@Valid @RequestBody RefreshTokenRequest refreshRequest) {
        return ResponseEntity.ok(authService.refreshToken(refreshRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.ok().build();
    }
}