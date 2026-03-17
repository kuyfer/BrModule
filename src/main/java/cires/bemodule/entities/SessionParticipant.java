package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class SessionParticipant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
    
    @ManyToOne
    @JoinColumn(name = "training_session_id")
    private TrainingSession trainingSession;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
}
