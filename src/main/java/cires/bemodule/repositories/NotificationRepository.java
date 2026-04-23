package cires.bemodule.repositories;

import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Override
    Optional<Notification> findById(Long aLong);

    List<Notification> findByToEmail(String toEmail);

    Set<Notification> findByNotificationStatus(NotificationStatus notificationStatus);

    Set<Notification> findByNotificationType(NotificationType notificationType);

}
