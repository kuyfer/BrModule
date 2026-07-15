package cires.bemodule.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Builder
@Audited @Entity
@Getter
@Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "session_participants")
public class SessionParticipant extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "training_session_id")
    private TrainingSession trainingSession;

    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;

    @OneToMany(mappedBy = "sessionParticipant")
    private List<Attendance> attendances = new ArrayList<>();

}