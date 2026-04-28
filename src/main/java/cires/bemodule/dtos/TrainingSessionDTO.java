package cires.bemodule.dtos;

import cires.bemodule.entities.SessionParticipant;
import cires.bemodule.entities.Subsidiary;
import cires.bemodule.entities.Trainer;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data @AllArgsConstructor @NoArgsConstructor
public class TrainingSessionDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String onSite;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TrainingSessionStatus status;
    private TrainingSessionMode mode;
    private Trainer trainer;
   // private Subsidiary subsidiary;
    private List<SessionParticipant> sessionParticipants;

}
