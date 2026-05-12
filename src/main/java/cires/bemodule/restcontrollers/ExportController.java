package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.*;
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
                                    @RequestParam(required = false) String email,
                                    @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<NotificationDTO> notifications = notificationService.findAll(type, status, email);

        String[] headers = {"Id", "Subject", "To Email", "Status", "Type"};
        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "notifications.xlsx", "Notifications", headers, notifications,
                    n -> new String[]{
                            String.valueOf(n.getId()),
                            n.getSubject(),
                            n.getToEmail(),
                            n.getNotificationStatus().toString(),
                            n.getNotificationType().toString()});
        } else {
            exportService.exportToCsv(response, "notifications.csv", headers, notifications,
                    n -> new String[]{
                            String.valueOf(n.getId()),
                            n.getSubject(),
                            n.getToEmail(),
                            n.getNotificationStatus().toString(),
                            n.getNotificationType().toString()});
        }
    }

    @GetMapping("/participants")
    public void exportParticipants(HttpServletResponse response,
                                   @RequestParam(required = false) RegistrationSource source,
                                   @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<ParticipantDTO> participants = participantService.findAll(source);

        String[] headers = {"Id", "First Name", "Last Name", "Email", "Registration Source"};
        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "participants.xlsx", "Participants", headers, participants,
                    participant -> new String[]{
                            String.valueOf(participant.getId()),
                            participant.getFirstName(),
                            participant.getLastName(),
                            participant.getEmail(),
                            participant.getRegistrationSource().toString()
                    });
        } else {
            exportService.exportToCsv(response, "participants.csv", headers, participants,
                    participant -> new String[]{
                            String.valueOf(participant.getId()),
                            participant.getFirstName(),
                            participant.getLastName(),
                            participant.getEmail(),
                            participant.getRegistrationSource().toString()
                    });
        }
    }

    @GetMapping("/sessions")
    public void exportSessions(HttpServletResponse response,
                               @RequestParam(required = false) TrainingSessionStatus status,
                               @RequestParam(required = false) TrainingSessionMode mode,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode);

        String[] headers = {"Id", "Name", "Start Date", "End Date", "Status", "Mode"};
        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "sessions.xlsx", "Training Sessions", headers, sessions,
                    session -> new String[]{
                            String.valueOf(session.getId()),
                            session.getTitle(),
                            session.getStartDate().toString(),
                            session.getEndDate().toString(),
                            session.getStatus().toString(),
                            session.getMode().toString()
                    });
        } else {
            exportService.exportToCsv(response, "sessions.csv", headers, sessions,
                    session -> new String[]{
                            String.valueOf(session.getId()),
                            session.getTitle(),
                            session.getStartDate().toString(),
                            session.getEndDate().toString(),
                            session.getStatus().toString(),
                            session.getMode().toString()
                    });
        }
    }

    @GetMapping("/users")
    public void exportUsers(HttpServletResponse response,
                            @RequestParam(required = false) AccountStatus status,
                            @RequestParam(required = false) String role,
                            @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<UserDTO> users = userService.findAll(role, status);

        String[] headers = {"Id", "Username", "First name", "Last name", "Email", "Role", "Status"};
        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "users.xlsx", "Users", headers, users,
                    user -> new String[]{
                            String.valueOf(user.getId()),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getRoles().toString(),
                            user.getAccountStatus().toString()
                    });
        } else {
            exportService.exportToCsv(response, "users.csv", headers, users,
                    user -> new String[]{
                            String.valueOf(user.getId()),
                            user.getUsername(),
                            user.getFirstName(),
                            user.getLastName(),
                            user.getEmail(),
                            user.getRoles().toString(),
                            user.getAccountStatus().toString()
                    });
        }
    }

    @GetMapping("/trainers")
    public void exportTrainers(HttpServletResponse response,
                               @RequestParam(required = false) String speciality,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<TrainerDTO> trainers = trainerService.findAll(speciality);

        String[] headers ={};
    }

}