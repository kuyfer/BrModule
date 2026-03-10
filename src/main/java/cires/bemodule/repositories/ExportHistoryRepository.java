package cires.bemodule.repositories;

import cires.bemodule.entities.ExportHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExportHistoryRepository extends JpaRepository<ExportHistory, Long> {
}
