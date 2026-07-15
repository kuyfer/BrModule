package cires.bemodule.utilities;

import cires.bemodule.repositories.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class ResetTokenCleanup {

    private final ResetTokenRepository resetTokenRepository;

    @Transactional
    @Scheduled(fixedDelay = 3_600_000) // 1 hour
    public void clearExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = resetTokenRepository.deleteByExpiresAtBefore(now);
        log.info("Cleared {} expired reset tokens", deletedCount);
    }
}