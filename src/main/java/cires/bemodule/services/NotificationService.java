package cires.bemodule.services;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.entities.Notification;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.exceptions.controllerexceptions.NotificationNotFoundException;
import cires.bemodule.mappers.NotificationMapper;
import cires.bemodule.repositories.NotificationRepository;
import cires.bemodule.specifications.NotificationSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public NotificationDTO findById(Long id) {
        logger.info("Finding notification by id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Notification not found with id: {}", id);
                    return new NotificationNotFoundException(id);
                });
        NotificationDTO dto = notificationMapper.toNotificationDto(notification);
        logger.info("Found notification with id: {}", id);
        return dto;
    }

    public List<NotificationDTO> findAll(NotificationType type, NotificationStatus status, String email) {
        logger.info("Finding all notifications with filters - type: {}, status: {}, email: {}", type, status, email);
        Specification<Notification> spec = Specification
                .where(NotificationSpecifications.hasType(type))
                .and(NotificationSpecifications.hasStatus(status))
                .and(NotificationSpecifications.toEmailEquals(email));

        List<Notification> notifications = notificationRepository.findAll(spec);
        List<NotificationDTO> dtos = notifications.stream()
                .map(notificationMapper::toNotificationDto)
                .toList();
        logger.info("Found {} notifications matching filters", dtos.size());
        return dtos;
    }
}