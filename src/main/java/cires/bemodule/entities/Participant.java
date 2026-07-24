package cires.bemodule.entities;

import cires.bemodule.enums.RegistrationSource;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

import java.util.ArrayList;
import java.util.List;

@Builder
@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "participants")
public class Participant extends Auditable {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegistrationSource registrationSource;

    @OneToMany(mappedBy = "participant")
    private List<SessionParticipant> sessionParticipants = new ArrayList<>();

}