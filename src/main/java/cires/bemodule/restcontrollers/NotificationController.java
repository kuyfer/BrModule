package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.NotificationDTO;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('notification:read')")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        NotificationDTO notification = notificationService.findById(id);
        return ResponseEntity.ok(notification);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('notification:read')")
    public ResponseEntity<Page<NotificationDTO>> getAllNotifications(
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(required = false) String email,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        Page<NotificationDTO> notifications = notificationService.findAll(type, status, email,pageable);
        return ResponseEntity.ok(notifications);
    }
}