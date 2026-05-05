package cires.bemodule.restcontrollers;

import cires.bemodule.dtos2.ResetRequest;
import cires.bemodule.repositories.ResetTokenRepository;
import cires.bemodule.repositories.UserRepository;
import cires.bemodule.services.PasswordResetService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;
import cires.bemodule.dtos2.ResetPasswordRequest;
import cires.bemodule.entities.ResetToken;
import cires.bemodule.entities.User;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/api/passwd")
public class PasswordResetController {

    private final PasswordResetService resetService;
    private final ResetTokenRepository resetTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetController(PasswordResetService resetService, ResetTokenRepository resetTokenRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.resetService = resetService;
        this.resetTokenRepository = resetTokenRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> requestReset(@Valid @RequestBody ResetRequest request) {
        resetService.processRequest(request.getEmail());
        return ResponseEntity.ok("If the email is registered, you'll get a reset link");
    }

    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        Optional<ResetToken> tokenOpt = resetTokenRepository.findByToken(request.getToken());

        if (tokenOpt.isEmpty() || tokenOpt.get().isExpired()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token is invalid or expired");
        }

        ResetToken tokenRecord = tokenOpt.get();
        User user = tokenRecord.getUser();

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        resetTokenRepository.delete(tokenRecord);

        return ResponseEntity.ok("Password updated");
    }
}
