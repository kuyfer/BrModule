package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.LoginRequest;
import cires.bemodule.dtos.requests.RefreshTokenRequest;
import cires.bemodule.dtos.requests.SetPasswordRequest;
import cires.bemodule.dtos.responses.AuthResponse;
import cires.bemodule.security.services.AuthService;
import cires.bemodule.services.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final PasswordResetService passwordResetService;

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

    /**
     * Verify if a password setup token is valid (not expired and exists in DB).
     * Called by the frontend when the user lands on the set-password page.
     */
    @GetMapping("/verify-token")
    public ResponseEntity<Map<String, Boolean>> verifyToken(@RequestParam String token) {
        boolean isValid = passwordResetService.isTokenValid(token);
        return ResponseEntity.ok(Map.of("valid", isValid));
    }

    /**
     * Set up a new password for the user.
     * Activates the account if it was in PENDING status.
     */
    @PostMapping("/setup-password")
    public ResponseEntity<Void> setupPassword(@RequestBody @Valid SetPasswordRequest request) {
        passwordResetService.setupPassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok().build();
    }
}