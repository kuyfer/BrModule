package cires.bemodule.repositories;

import cires.bemodule.entities.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long>, JpaSpecificationExecutor<Trainer> {

    boolean existsByUserId(Long userId);
    Optional<Trainer> findByUserId(Long userId);

}
