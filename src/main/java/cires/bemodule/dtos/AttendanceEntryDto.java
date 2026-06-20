package cires.bemodule.dtos;

import cires.bemodule.enums.AttendanceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class AttendanceEntryDto {
    @NotNull
    private Long participantId;

    @NotNull
    private AttendanceStatus status;
}
