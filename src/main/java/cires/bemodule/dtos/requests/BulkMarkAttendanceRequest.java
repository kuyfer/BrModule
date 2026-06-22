package cires.bemodule.dtos.requests;

import cires.bemodule.enums.AttendanceSlot;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class BulkMarkAttendanceRequest {

    @NotNull
    private Long sessionId;

    @NotNull
    private LocalDate date;

    @NotNull
    private AttendanceSlot slot;

    @NotEmpty(message = "Entries list must not be empty")
    private List<BulkAttendanceEntry> entries;
}