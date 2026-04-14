package cires.bemodule.utilities;

import cires.bemodule.repositories.ResetTokenRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class ResetTokenCleanup {
    private final ResetTokenRepository resetTokenRepository;

    public ResetTokenCleanup(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Scheduled(fixedRate = 3_600_000) // runs every hour
    public void clearExpiredTokens() {
        // TODO : use a better delete with expiry date in mind
        resetTokenRepository.deleteAll();
    }
}
