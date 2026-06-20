package cires.bemodule.dtos.requests;

import cires.bemodule.dtos.AttendanceEntryDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class AttendanceRequest {

    @NotNull
    private Long sessionId;

    @NotBlank @Pattern(regexp = "AM|PM", message = "Period must be AM or PM")
    private String period;

    @NotEmpty @Valid
    private List<AttendanceEntryDto> entries;
}
