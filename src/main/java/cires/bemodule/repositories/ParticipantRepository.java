package cires.bemodule.repositories;

import cires.bemodule.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    @Override
    Optional<Participant> findById(Long aLong);

}
