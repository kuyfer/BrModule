package cires.bemodule.repositories;

import cires.bemodule.entities.Subsidiary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubsidiaryRepository extends JpaRepository<Subsidiary, Long> {

   Optional<Subsidiary> findByName(String name);

    boolean existsByName(String name);

    Page<Subsidiary> findByNameContainingIgnoreCase(String name, Pageable pageable);
}