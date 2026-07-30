package cires.bemodule.repositories;

import cires.bemodule.entities.ExportHistory;
import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.Set;

@Repository
public interface ExportHistoryRepository extends JpaRepository<ExportHistory, Long>, JpaSpecificationExecutor<ExportHistory> {
}