package cires.bemodule.repositories;

import cires.bemodule.entities.Subsidiary;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.TrainingSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long>, JpaSpecificationExecutor<TrainingSession> {

    //Optional<TrainingSession> findBySubsidiary(Subsidiary subsidiary);

    Optional<TrainingSession> findByTrainer(Trainer trainer);

}
