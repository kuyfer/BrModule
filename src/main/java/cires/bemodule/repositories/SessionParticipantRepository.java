package cires.bemodule.repositories;

import cires.bemodule.entities.SessionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionParticipantRepository extends JpaRepository<SessionParticipant, Long> {

    @Override
    Optional<SessionParticipant> findById(Long aLong);

}