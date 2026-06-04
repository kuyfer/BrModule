package cires.bemodule.dtos.views;

import cires.bemodule.entities.SessionParticipant;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class TrainingSessionDTO {

    private Long id;
    private String title;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private TrainingSessionStatus status;
    private TrainingSessionMode mode;
    private TrainerDTO trainer;
   // private Subsidiary subsidiary;
    private List<SessionParticipant> sessionParticipants;

}
