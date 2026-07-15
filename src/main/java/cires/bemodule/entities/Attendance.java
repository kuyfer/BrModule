package cires.bemodule.entities;

import cires.bemodule.enums.AttendanceSlot;
import cires.bemodule.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "attendance")
public class Attendance extends Auditable{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "session_participant_id")
    private SessionParticipant sessionParticipant;

    private String period;

    private String sessionTitle;

    @Column(nullable = false)
    private boolean validated = false;

    @Column(name = "validated_by")
    private Long validatedBy;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Enumerated(EnumType.STRING)
    private AttendanceSlot slot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private AttendanceStatus status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "session_id", nullable = false)
    private TrainingSession session;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "delay_reason")
    private String delayReason;

    @Column
    private String comment;

    @Column(name = "correction_reason")
    private String correctionReason;

    @Column(name = "audit_note", columnDefinition = "TEXT")
    private String auditNote;
}