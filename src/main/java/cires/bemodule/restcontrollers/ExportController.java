package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.NotificationDTO;
import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.enums.*;
import cires.bemodule.services.*;
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
    private final TrainingSessionService trainingSessionService;
    private final TrainerService trainerService;

    public ExportController(ExportService exportService,
                            NotificationService notificationService,
                            UserService userService, ParticipantService participantService, TrainingSessionService trainingSessionService, TrainerService trainerService) {
        this.exportService = exportService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.participantService = participantService;
        this.trainingSessionService = trainingSessionService;
        this.trainerService = trainerService;
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
                               @RequestParam(required = false) TrainingSessionStatus status,
                               @RequestParam(required = false) TrainingSessionMode mode) throws IOException {
        List<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode);

        String[] headers ={"Id", "Name", "Start Date", "End Date", "Status", "Mode"};
        exportService.exportToCsv(response, "sessions.csv", headers, sessions,
                session -> new String[]{
                        String.valueOf(session.getId()),
                        session.getTitle(),
                        session.getStartDate().toString(),
                        session.getEndDate().toString(),
                        session.getStatus().toString(),
                        session.getMode().toString()
                }
                );
    }

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