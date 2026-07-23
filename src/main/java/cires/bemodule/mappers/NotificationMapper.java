package cires.bemodule.mappers;

import cires.bemodule.dtos.views.NotificationDTO;
import cires.bemodule.entities.Notification;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    NotificationDTO toNotificationDto(Notification notification);

    Notification toNotification(NotificationDTO notificationDTO);
}