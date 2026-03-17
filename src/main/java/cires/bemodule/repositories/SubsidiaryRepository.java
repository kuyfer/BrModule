package cires.bemodule.repositories;

import cires.bemodule.entities.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    @Override
    Optional<Subsidiary> findById(Long aLong);

    Subsidiary findByName(String name);

}
