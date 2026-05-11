package cires.bemodule.repositories;

import cires.bemodule.entities.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {
}