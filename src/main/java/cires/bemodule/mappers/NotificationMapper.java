package cires.bemodule.mappers;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.entities.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "notificationType", target = "notificationType")
    @Mapping(source = "notificationStatus", target = "notificationStatus")
    NotificationDTO toNotificationDto(Notification notification);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "notificationType", target = "notificationType")
    @Mapping(source = "notificationStatus", target = "notificationStatus")
    Notification toNotification(NotificationDTO notificationDTO);

}
