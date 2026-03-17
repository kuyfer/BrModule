package cires.bemodule.entities;


import cires.bemodule.enums.TrainingSessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingSessionStatus status;

    @Column(nullable = false)
    private String title;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToOne
    @JoinColumn(name = "subsidiary_id")
    private Subsidiary subsidiary;

    @OneToMany(mappedBy = "trainingSession")
    private List<SessionParticipant> sessionParticipants = new ArrayList<>();


}
