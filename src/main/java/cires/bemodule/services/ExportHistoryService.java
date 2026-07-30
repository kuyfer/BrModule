package cires.bemodule.services;

import cires.bemodule.dtos.views.ExportHistoryDTO;
import cires.bemodule.entities.ExportHistory;
import cires.bemodule.enums.ExportFormat;
import cires.bemodule.enums.ExportStatus;
import cires.bemodule.mappers.ExportHistoryMapper;
import cires.bemodule.repositories.ExportHistoryRepository;
import cires.bemodule.specifications.ExportHistorySpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExportHistoryService {

    private final ExportHistoryRepository exportHistoryRepository;
    private final ExportHistoryMapper exportHistoryMapper;


    @Transactional
    public void recordExport(String username, String entityType, ExportFormat format, String fileName) {
        ExportHistory history = ExportHistory.builder()
                .exportedBy(username)
                .exportedAt(LocalDateTime.now())
                .entityType(entityType)
                .exportFormat(format)
                .exportStatus(ExportStatus.SUCCESS)
                .fileName(fileName)
                .build();
        exportHistoryRepository.save(history);
        log.info("Export recorded: user={}, entity={}, format={}", username, entityType, format);
    }

    @Transactional(readOnly = true)
    public Page<ExportHistoryDTO> findFiltered(String username, String entityType,
                                               ExportFormat format, LocalDateTime from, LocalDateTime to,
                                               Pageable pageable) {

        Specification<ExportHistory> spec = Specification
                .where(ExportHistorySpecifications.hasUsername(username))
                .and(ExportHistorySpecifications.hasEntityType(entityType))
                .and(ExportHistorySpecifications.hasFormat(format))
                .and(ExportHistorySpecifications.exportedAfter(from))
                .and(ExportHistorySpecifications.exportedBefore(to));

        return exportHistoryRepository.findAll(spec, pageable).map(exportHistoryMapper::toExportDto);
    }
}