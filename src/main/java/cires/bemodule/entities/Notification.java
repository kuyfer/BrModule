package cires.bemodule.entities;

import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.Audited;

@Builder
@Audited @Entity
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "notifications")
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationStatus notificationStatus;

    @Column(nullable = false)
    private String toEmail;

    @Column(nullable = false)
    private String subject;

    private String failureReason;

}