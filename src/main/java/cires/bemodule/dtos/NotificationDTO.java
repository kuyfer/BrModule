package cires.bemodule.dtos;

import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor@NoArgsConstructor
public class NotificationDTO {

    private Long id;
    private NotificationType notificationType;
    private NotificationStatus notificationStatus;

}
