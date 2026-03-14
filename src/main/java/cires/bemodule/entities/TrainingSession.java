package cires.bemodule.entities;


import cires.bemodule.enums.TrainingSessionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "Session")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrainingSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TrainingSessionStatus status;

    // TODO :  formateur et rattachement organisationnel

}
