package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.requests.ResetPasswordRequest;
import cires.bemodule.dtos.requests.ResetRequest;
import cires.bemodule.services.PasswordResetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/passwd")
public class PasswordResetController {

    private final PasswordResetService resetService;

    @PostMapping("/reset-password")
    public ResponseEntity<String> requestReset(@Valid @RequestBody ResetRequest request) {
        resetService.processRequest(request.getEmail());
        return ResponseEntity.ok("If the email is registered, you'll get a reset link");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        resetService.resetPassword(request.getToken(), request.getPassword());
        return ResponseEntity.ok("Password updated");
    }
}