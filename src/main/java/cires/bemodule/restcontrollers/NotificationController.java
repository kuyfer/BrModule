package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.services.NotificationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
            List<NotificationDTO> notifications = notificationService.findAll();
            return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.findById(id);
        return ResponseEntity.ok(notification);
    }


    @PostMapping("/send")
    public void sendNotification(@RequestBody Object notification) {}

    @PostMapping("/{id}/retry")
    public void retryNotification(@PathVariable Long id) {}
}