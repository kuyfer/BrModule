package cires.bemodule.services;

import cires.bemodule.entities.ResetToken;
import cires.bemodule.entities.User;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.ResetTokenRepository;
import cires.bemodule.repositories.UserRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class PasswordResetService {
// FIXME : this blocks from deleting users for sql constraints

    private final UserRepository userRepository;
    private final ResetTokenRepository resetTokenRepository;
    private final EmailQueueProducer emailQueueProducer;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder =
            Base64.getUrlEncoder().withoutPadding();

    public PasswordResetService(UserRepository userRepository, ResetTokenRepository resetTokenRepository, EmailQueueProducer emailQueueProducer) {
        this.userRepository = userRepository;
        this.resetTokenRepository = resetTokenRepository;
        this.emailQueueProducer = emailQueueProducer;
    }

    public String makeResetToken() {
        byte[] randomBytes = new byte[4];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    public void processRequest(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return;

        User user = userOpt.get();
        String token = makeResetToken();

        ResetToken resetTokenEntity = new ResetToken();
        resetTokenEntity.setToken(token);
        resetTokenEntity.setUser(user);
        resetTokenEntity.setExpiresAt(LocalDateTime.now().plusMinutes(60));

        resetTokenRepository.save(resetTokenEntity);

        sendResetEmail(user.getEmail(), token);
    }

    public void sendResetEmail(String email, String token) {
        Map<String, Object> model = new HashMap<>();
        model.put("token", token);
        model.put("email", email);

        EmailPayload payload = new EmailPayload(
                email,
                "Password Reset Request",
                "password-reset",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.PASSWORD_RESET);
    }

}
