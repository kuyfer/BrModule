package cires.bemodule.services;

import cires.bemodule.entities.ResetToken;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.repositories.ResetTokenRepository;
import cires.bemodule.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class PasswordResetService {

    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder =
            Base64.getUrlEncoder().withoutPadding();

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void processRequest(String email) {
        log.info("Processing password reset request for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            log.debug("No user found for password reset with email: {}", email);
            return;
        }

        User user = userOpt.get();
        String token = makeResetToken();
        log.debug("Generated reset token for user id: {}", user.getId());

        ResetToken resetTokenEntity = new ResetToken();
        resetTokenEntity.setToken(token);
        resetTokenEntity.setUser(user);
        resetTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(60));

        resetTokenRepository.save(resetTokenEntity);
        log.info("Reset token saved for user id: {}, expires at: {}", user.getId(), resetTokenEntity.getExpiresAt());

        notificationService.sendResetEmail(user.getEmail(), token);
    }

    public void generateAndSendPasswordSetupToken(User user) {
        String token = makeResetToken();
        log.debug("Generated password-setup token for user id: {}", user.getId());

        ResetToken resetTokenEntity = new ResetToken();
        resetTokenEntity.setToken(token);
        resetTokenEntity.setUser(user);
        resetTokenEntity.setExpiresAt(LocalDateTime.now().plusHours(24));

        resetTokenRepository.save(resetTokenEntity);
        log.info("Password-setup token saved for user id: {}, expires at: {}", user.getId(), resetTokenEntity.getExpiresAt());

        String setupLink = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/set-password")
                .queryParam("token", token)
                .build()
                .toUriString();
        notificationService.sendPasswordSetupEmail(user, setupLink);
    }

    public void setupPassword(String token, String newPassword) {
        log.info("Setting up password for token: {}", token);

        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired token"));

        if (resetToken.isExpired()) {
            throw new IllegalArgumentException("Token has expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));


        if (user.getAccountStatus() == AccountStatus.PENDING) {
            user.setAccountStatus(AccountStatus.ACTIVE);
            log.info("User account activated: {}", user.getUsername());
        }

        userRepository.save(user);
        resetTokenRepository.delete(resetToken);

        log.info("Password successfully set for user: {}", user.getUsername());
    }

    public boolean isTokenValid(String token) {
        return resetTokenRepository.findByToken(token)
                .map(rt -> !rt.isExpired())
                .orElse(false);
    }

    // ################################# UTILS ######################################

    private String makeResetToken() {
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        log.debug("Generated raw token (for internal use)");
        return token;
    }
}