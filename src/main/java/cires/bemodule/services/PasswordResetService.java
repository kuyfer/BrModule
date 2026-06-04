package cires.bemodule.services;

import cires.bemodule.entities.ResetToken;
import cires.bemodule.entities.User;
import cires.bemodule.repositories.ResetTokenRepository;
import cires.bemodule.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class PasswordResetService {
// FIXME : this blocks from deleting users for sql constraints

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetService.class);

    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final EmailQueueProducer emailQueueProducer;
    private final NotificationService notificationService;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder =
            Base64.getUrlEncoder().withoutPadding();

    public PasswordResetService(UserRepository userRepository, ResetTokenRepository resetTokenRepository, EmailQueueProducer emailQueueProducer, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.emailQueueProducer = emailQueueProducer;
        this.notificationService = notificationService;
    }

    public void processRequest(String email) {
        logger.info("Processing password reset request for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            logger.debug("No user found for password reset with email: {}", email);
            return;
        }

        User user = userOpt.get();
        String token = makeResetToken();
        logger.debug("Generated reset token for user id: {}", user.getId());

        ResetToken resetTokenEntity = new ResetToken();
        resetTokenEntity.setToken(token);
        resetTokenEntity.setUser(user);
        resetTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(60));

        resetTokenRepository.save(resetTokenEntity);
        logger.info("Reset token saved for user id: {}, expires at: {}", user.getId(), resetTokenEntity.getExpiresAt());

        notificationService.sendResetEmail(user.getEmail(), token);
    }

    // ################################# UTILS ######################################

    private String makeResetToken() {
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        String token = base64Encoder.encodeToString(randomBytes);
        logger.debug("Generated raw token (for internal use)");
        return token;
    }
}