package cires.bemodule.mappers;

import cires.bemodule.dtos.ExportHistoryDTO;
import cires.bemodule.entities.ExportHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExportHistoryMapper {

    ExportHistoryDTO toExportDto(ExportHistory exportHistory);

    ExportHistory toExportHistory(ExportHistoryDTO exportDTO);

}
