package cires.bemodule.repositories;

import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer, Long> {

    @Override
    Optional<Trainer> findById(Long aLong);

    Optional<Trainer> findByUser(User user);

    Optional<Trainer> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}
