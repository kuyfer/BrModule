package cires.bemodule.dtos.views;

import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import lombok.*;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private NotificationType notificationType;
    private NotificationStatus notificationStatus;
    private String toEmail;
    private String subject;
    private String failureReason;

}
