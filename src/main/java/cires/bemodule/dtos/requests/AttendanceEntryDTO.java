package cires.bemodule.dtos.requests;

import cires.bemodule.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entry for a single participant’s attendance status in a bulk or single request.
 * Used inside {@link AttendanceRequest} and {@link BulkAttendanceEntry}.
 */
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AttendanceEntryDTO {
    @NotNull
    private Long participantId;

    @NotNull
    private AttendanceStatus status;
}
