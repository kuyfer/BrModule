package cires.bemodule.repositories;

import cires.bemodule.entities.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    List<Long> findParticipantIdsByTrainingSessionId(Long sessionId);

    boolean existsByTrainingSessionIdAndParticipantId(Long sessionId, Long participantId);
}