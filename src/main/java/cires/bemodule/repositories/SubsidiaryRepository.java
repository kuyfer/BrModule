package cires.bemodule.repositories;

import cires.bemodule.entities.Subsidiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

    Subsidiary findByName(String name);

}
