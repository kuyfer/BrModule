package cires.bemodule.utilities;

import cires.bemodule.repositories.ResetTokenRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@Configuration
@EnableScheduling
public class ResetTokenCleanup {

    Logger logger = LoggerFactory.getLogger(ResetTokenCleanup.class);

    private final ResetTokenRepository resetTokenRepository;

    public ResetTokenCleanup(ResetTokenRepository resetTokenRepository) {
        this.resetTokenRepository = resetTokenRepository;
    }

    @Scheduled(fixedRate = 3_600_000) // runs every hour
    public void clearExpiredTokens() {
        logger.info("Clearing expired reset tokens");
        resetTokenRepository.deleteAll();
    }
}
