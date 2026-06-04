package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.NotificationDTO;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.services.NotificationService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(
            @RequestParam(required = false) NotificationType type,
            @RequestParam(required = false) NotificationStatus status,
            @RequestParam(required = false) String email
    ) {
        List<NotificationDTO> notifications = notificationService.findAll(type, status, email);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/export")
    public void exportNotifications(HttpServletResponse response,
                                    @RequestParam(required = false) NotificationType type,
                                    @RequestParam(required = false) NotificationStatus status,
                                    @RequestParam(required = false) String email

    ) throws IOException {
        response.setContentType("text/csv; charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=\"notification.csv\"");

        try (PrintWriter writer = response.getWriter()) {
            writer.println("Id,Subject,To,Status,Type");

            List<NotificationDTO> notifications = notificationService.findAll(type, status, email);
            for (NotificationDTO notification : notifications) {
                String row = String.format(Locale.ROOT, "%d,%s,%s, %s, %s",
                        notification.getId(),
                        escapeCsv(notification.getToEmail()),
                        escapeCsv(notification.getNotificationStatus().toString()),
                        escapeCsv(notification.getNotificationType().toString()),
                        escapeCsv(notification.getSubject())
                );
                writer.println(row);
            }
        }
    }

    private String escapeCsv(String input) {
        if (input == null) {
            return "";
        }
        if (input.contains(",") || input.contains("\"") || input.contains("\n") || input.contains("\r")) {
            return "\"" + input.replace("\"", "\"\"") + "\"";
        }
        return input;
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