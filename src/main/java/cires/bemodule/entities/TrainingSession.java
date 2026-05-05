package cires.bemodule.entities;

import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Session")
public class TrainingSession {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingSessionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingSessionMode mode;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

//    @ManyToOne
//    @JoinColumn(name = "subsidiary_id")
//    private Subsidiary subsidiary;

    @OneToMany(mappedBy = "trainingSession")
    private List<SessionParticipant> sessionParticipants = new ArrayList<>();

}
