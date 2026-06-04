package cires.bemodule.repositories;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TrainingSessionRepository extends JpaRepository<TrainingSession, Long>, JpaSpecificationExecutor<TrainingSession> {

    List<TrainingSession> findByStatusAndStartDateLessThanEqual(TrainingSessionStatus trainingSessionStatus, LocalDateTime now);

    List<TrainingSession> findByStatusAndEndDateLessThanEqual(TrainingSessionStatus trainingSessionStatus, LocalDateTime now);

    List<TrainingSession> findByStatusAndStartDateBetween(TrainingSessionStatus trainingSessionStatus, LocalDateTime from, LocalDateTime to);

}

