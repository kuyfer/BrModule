package cires.bemodule.mappers;

import cires.bemodule.dtos.ExportHistoryDTO;
import cires.bemodule.entities.ExportHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExportHistoryMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "exportStatus", target = "exportStatus")
    @Mapping(source = "exportFormat", target = "exportFormat")
    ExportHistoryDTO toExportDto(ExportHistory exportHistory);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "exportStatus", target = "exportStatus")
    @Mapping(source = "exportFormat", target = "exportFormat")
    ExportHistory toExportHistory(ExportHistoryDTO exportDTO);

}
