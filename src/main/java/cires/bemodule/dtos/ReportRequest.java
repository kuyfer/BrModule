package cires.bemodule.dtos;

import cires.bemodule.enums.ReportFormat;
import cires.bemodule.enums.ReportType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReportRequest {

    @NotNull
    private ReportType reportType;

    @NotNull
    private ReportFormat format;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private Long subsidiaryId;
    private Long trainerId;
}