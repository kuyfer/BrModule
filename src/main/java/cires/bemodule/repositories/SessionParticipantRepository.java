package cires.bemodule.repositories;

import cires.bemodule.entities.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    @Query("SELECT sp.participant.id FROM SessionParticipant sp WHERE sp.trainingSession.id = :sessionId")
    List<Long> findParticipantIdByTrainingSessionId(Long sessionId);

    boolean existsByTrainingSessionIdAndParticipantId(Long sessionId, Long participantId);
}