package cires.bemodule.utilities;

import cires.bemodule.repositories.ResetTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Spring {@link Configuration} that periodically removes expired
 * {@link cires.bemodule.entities.ResetToken} records from the database.
 * <p>
 * The cleanup runs once every hour (3,600,000 ms) after the previous
 * invocation completes.  Because expired tokens are only useful for a
 * limited time, this scheduled task keeps the table small and ensures
 * that expired tokens are not accidentally re‑used.
 * </p>
 */
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@Configuration
public class ResetTokenCleanup {

    private final ResetTokenRepository resetTokenRepository;

    /**
     * Deletes all reset tokens whose {@code expiresAt} timestamp is before
     * the current instant.
     * <p>
     * Executed with a fixed delay of one hour. The operation is wrapped in a
     * transaction; if the deletion fails for any reason the transaction is
     * rolled back and the error is logged, leaving the tokens in place for a
     * subsequent cleanup attempt.
     * </p>
     */
    @Transactional
    @Scheduled(fixedDelay = 3_600_000)
    public void clearExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = resetTokenRepository.deleteByExpiresAtBefore(now);
        log.info("Cleared {} expired reset tokens", deletedCount);
    }
}