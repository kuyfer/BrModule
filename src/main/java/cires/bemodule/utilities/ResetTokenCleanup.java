package cires.bemodule.utilities;

import cires.bemodule.repositories.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class ResetTokenCleanup {

    private final ResetTokenRepository resetTokenRepository;


    @Scheduled(fixedRate = 3_600_000) // runs every hour
    public void clearExpiredTokens() {
        log.info("Clearing expired reset tokens");
        resetTokenRepository.deleteAll();
    }
}
