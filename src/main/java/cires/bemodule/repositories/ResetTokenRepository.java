package cires.bemodule.repositories;

import cires.bemodule.entities.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {

    int deleteByExpiresAtBefore(LocalDateTime now);

    Optional<ResetToken> findByToken(String token);
}