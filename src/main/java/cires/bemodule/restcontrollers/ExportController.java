package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.enums.NotificationStatus;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.services.ExportService;
import cires.bemodule.services.NotificationService;
import cires.bemodule.services.ParticipantService;
import cires.bemodule.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportService exportService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ParticipantService participantService;
    // Add other services (TrainerService, etc.) as needed

    public ExportController(ExportService exportService,
                            NotificationService notificationService,
                            UserService userService, ParticipantService participantService) {
        this.exportService = exportService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.participantService = participantService;
    }

    @GetMapping("/notifications")
    public void exportNotifications(HttpServletResponse response,
                                    @RequestParam(required = false) NotificationType type,
                                    @RequestParam(required = false) NotificationStatus status,
                                    @RequestParam(required = false) String email) throws IOException {

        List<NotificationDTO> notifications = notificationService.findAll(type, status, email);

        String[] headers = {"Id", "Subject", "To Email", "Status", "Type"};
        exportService.exportToCsv(response, "notifications.csv", headers, notifications,
                notification -> new String[]{
                        String.valueOf(notification.getId()),
                        notification.getSubject(),
                        notification.getToEmail(),
                        notification.getNotificationStatus().toString(),
                        notification.getNotificationType().toString()
                });
    }

    @GetMapping("/participants")
    public void exportParticipants(HttpServletResponse response,
                                    @RequestParam(required = false) RegistrationSource source) throws IOException {
        List<ParticipantDTO> participants = participantService.findAll(source);

        String[] headers = {"Id", "First Name", "Last Name", "Email", "Registration Source"};
        exportService.exportToCsv(response, "participants.csv", headers, participants,
                participant -> new String[]{
                        String.valueOf(participant.getId()),
                        participant.getFirstName(),
                        participant.getLastName(),
                        participant.getEmail(),
                        participant.getRegistrationSource()
                });
    }

    @GetMapping("/sessions")
    public void exportSessions(HttpServletResponse response,
                               @RequestParam(required = false) String status,
                               @RequestParam(required = false) String mode) throws IOException {}

    @GetMapping("/trainers")
    public void exportTrainers(HttpServletResponse response,
                               @RequestParam(required = false) String speciality) throws IOException {}

//    @GetMapping("/users")
//    public void exportUsers(HttpServletResponse response,
//                            @RequestParam(required = false) String role) throws IOException {
//
//        List<UserDTO> users = userService.findAllByRole(role); // you'll add this method
//
//        String[] headers = {"Id", "Username", "First Name", "Last Name", "Email", "Account Status"};
//        csvExportService.exportToCsv(response, "users.csv", headers, users,
//                user -> new String[]{
//                        String.valueOf(user.getId()),
//                        user.getUsername(),
//                        user.getFirstName(),
//                        user.getLastName(),
//                        user.getEmail(),
//                        user.getAccountStatus()
//                });
//    }

    // 3. Export Trainers (if you have a TrainerService/DTO)
    // @GetMapping("/trainers")
    // public void exportTrainers(...) { ... }
}