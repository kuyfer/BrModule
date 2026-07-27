package cires.bemodule.services;

import cires.bemodule.entities.ResetToken;
import cires.bemodule.entities.User;
import cires.bemodule.enums.AccountStatus;
import cires.bemodule.exceptions.business.BadRequestException;
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

/**
 * Service responsible for all password‑related operations:
 * <ul>
 *   <li>Requesting a password reset token</li>
 *   <li>Generating a password setup token for new accounts</li>
 *   <li>Setting up an initial password (activating the account)</li>
 *   <li>Resetting an existing password using a reset token</li>
 *   <li>Verifying token validity</li>
 * </ul>
 *
 * Tokens are short‑lived random strings stored in {@link ResetToken} entities.
 */
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

    /**
     * Processes a password reset request.
     * <p>
     * If a user with the given email exists, a unique reset token is generated,
     * persisted with a 60‑minute expiry, and sent to the user via email.
     * If no matching user is found, the request is silently ignored to avoid
     * user enumeration.
     * </p>
     *
     * @param email the user’s email address
     */
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

        String resetLink = UriComponentsBuilder.fromUriString(frontendUrl)
                .path("/reset-password")
                .queryParam("token", token)
                .build()
                .toUriString();

        notificationService.sendResetEmail(user.getEmail(), resetLink);
    }

    /**
     * Generates a password setup token for a new user and sends a setup link via email.
     * <p>
     * The token is valid for 24 hours. The link is built using the configured
     * {@code app.frontend-url} and the token, pointing to the frontend’s
     * password setup page.
     * </p>
     *
     * @param user the newly created user who needs to set a password
     */
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

    /**
     * Sets the initial password for a user and activates the account if it was
     * in {@code PENDING} status.
     * <p>
     * The supplied token must be valid and non‑expired. After a successful
     * password setup, the token is deleted.
     * </p>
     *
     * @param token       the password setup token (from the email link)
     * @param newPassword the chosen plain‑text password
     * @throws IllegalArgumentException if the token is invalid or expired
     */
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

    /**
     * Resets the password for an existing user using a password reset token.
     * <p>
     * Validates the token, updates the user's password, and deletes the token.
     * </p>
     *
     * @param token       the reset token string
     * @param newPassword the new plain‑text password
     * @throws BadRequestException if the token is invalid or expired
     */
    public void resetPassword(String token, String newPassword) {
        log.info("Resetting password for token: {}", token);
        ResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new BadRequestException("Invalid or expired token"));

        if (resetToken.isExpired()) {
            throw new BadRequestException("Token has expired");
        }
        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        resetTokenRepository.delete(resetToken);
    }

    /**
     * Checks whether a given token is still valid (exists and not expired).
     *
     * @param token the token to verify
     * @return {@code true} if the token is valid, {@code false} otherwise
     */
    public boolean isTokenValid(String token) {
        return resetTokenRepository.findByToken(token)
                .map(rt -> !rt.isExpired())
                .orElse(false);
    }

    // ################################# UTILS ######################################

    /**
     * Generates a cryptographically secure random token (URL‑safe Base64, no padding).
     *
     * @return a 6‑character token string (4 random bytes → ~6 Base64 characters)
     */
    private String makeResetToken() {
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        log.debug("Generated raw token (for internal use)");
        return token;
    }
}