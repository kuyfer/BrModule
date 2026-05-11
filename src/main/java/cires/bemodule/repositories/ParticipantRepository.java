package cires.bemodule.repositories;

import cires.bemodule.entities.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long>, JpaSpecificationExecutor<Participant> {

}
