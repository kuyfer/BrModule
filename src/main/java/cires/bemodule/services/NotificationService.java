package cires.bemodule.services;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.exceptions.controllerexceptions.NotificationNotFoundException;
import cires.bemodule.mappers.NotificationMapper;
import cires.bemodule.repositories.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;

    public NotificationService(NotificationRepository notificationRepository, NotificationMapper notificationMapper) {
        this.notificationRepository = notificationRepository;
        this.notificationMapper = notificationMapper;
    }

    public NotificationDTO findById(Long id){
        return  notificationMapper.toNotificationDto(notificationRepository.findById(id).orElseThrow(() -> new NotificationNotFoundException( id)));
    }

    public List<NotificationDTO> findAll(){
        return notificationRepository.findAll()
                .stream()
                .map(notificationMapper::toNotificationDto)
                .toList();
    }

    public List<NotificationDTO> findByRecipiant(String email){
        return  notificationRepository.findByToEmail(email)
                .stream()
                .map(notificationMapper::toNotificationDto)
                .collect(Collectors.toList());
    }
}
