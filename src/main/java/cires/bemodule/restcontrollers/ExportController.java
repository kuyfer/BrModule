package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.*;
import cires.bemodule.enums.*;
import cires.bemodule.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportService exportService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ParticipantService participantService;
    private final TrainingSessionService trainingSessionService;
    private final TrainerService trainerService;

    private static final String[] NOTIFICATION_HEADERS = {"Id", "Subject", "To Email", "Status", "Type"};
    private static final String[] PARTICIPANT_HEADERS = {"Id", "First Name", "Last Name", "Email", "Registration Source"};
    private static final String[] SESSION_HEADERS = {"Id", "Name", "Start Date", "End Date", "Status", "Mode"};
    private static final String[] USER_HEADERS = {"Id", "Username", "First name", "Last name", "Email", "Role", "Status"};
    private static final String[] TRAINER_HEADERS = {"Id", "User Name", "First Name", "Last Name", "Email", "Speciality", "Status"};

    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('notification:read')")
    public void exportNotifications(HttpServletResponse response,
                                    @RequestParam(required = false) NotificationType type,
                                    @RequestParam(required = false) NotificationStatus status,
                                    @RequestParam(required = false) String email,
                                    @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<NotificationDTO> notifications = notificationService.findAll(type, status, email);

        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "notifications.xlsx", "Notifications", NOTIFICATION_HEADERS, notifications,
                    n -> new String[]{
                            String.valueOf(n.getId()),
                            n.getSubject(),
                            n.getToEmail(),
                            n.getNotificationStatus().toString(),
                            n.getNotificationType().toString()});
        } else {
            exportService.exportToCsv(response, "notifications.csv", NOTIFICATION_HEADERS, notifications,
                    n -> new String[]{
                            String.valueOf(n.getId()),
                            n.getSubject(),
                            n.getToEmail(),
                            n.getNotificationStatus().toString(),
                            n.getNotificationType().toString()});
        }
    }

    @GetMapping("/participants")
    @PreAuthorize("hasAuthority('participant:read')")
    public void exportParticipants(HttpServletResponse response,
                                   @RequestParam(required = false) RegistrationSource source,
                                   @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<ParticipantDTO> participants = participantService.findAll(source);

        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "participants.xlsx", "Participants", PARTICIPANT_HEADERS, participants,
                    participant -> new String[]{
                            String.valueOf(participant.getId()),
                            participant.getFirstName(),
                            participant.getLastName(),
                            participant.getEmail(),
                            participant.getRegistrationSource().toString()
                    });
        } else {
            exportService.exportToCsv(response, "participants.csv", PARTICIPANT_HEADERS, participants,
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
    @PreAuthorize("hasAuthority('session:read')")
    public void exportSessions(HttpServletResponse response,
                               @RequestParam(required = false) TrainingSessionStatus status,
                               @RequestParam(required = false) TrainingSessionMode mode,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<TrainingSessionDTO> sessions = trainingSessionService.findAll(status, mode);

        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "sessions.xlsx", "Training Sessions", SESSION_HEADERS, sessions,
                    session -> new String[]{
                            String.valueOf(session.getId()),
                            session.getTitle(),
                            session.getStartDate().toString(),
                            session.getEndDate().toString(),
                            session.getStatus().toString(),
                            session.getMode().toString()
                    });
        } else {
            exportService.exportToCsv(response, "sessions.csv", SESSION_HEADERS, sessions,
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
    @PreAuthorize("hasAuthority('user:read')")
    public void exportUsers(HttpServletResponse response,
                            @RequestParam(required = false) AccountStatus status,
                            @RequestParam(required = false) String role,
                            @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {

        List<UserDTO> users = userService.findAll(role, status);

        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "users.xlsx", "Users", USER_HEADERS, users,
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
            exportService.exportToCsv(response, "users.csv", USER_HEADERS, users,
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
    @PreAuthorize("hasAuthority('trainer:read')")
    public void exportTrainers(HttpServletResponse response,
                               @RequestParam(required = false) String speciality,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<TrainerDTO> trainers = trainerService.findAll(speciality);

        if (format == ExportFormat.EXCEL) {
            exportService.exportToExcel(response, "trainers.xlsx", "Trainers", TRAINER_HEADERS, trainers,
                    trainer -> new String[]{
            String.valueOf(trainer.getId()),
                    trainer.getUser().getUsername(),
                    trainer.getUser().getFirstName(),
                    trainer.getUser().getLastName(),
                    trainer.getUser().getEmail(),
                    trainer.getSpeciality(),
                    trainer.getUser().getAccountStatus().toString()
                    });
        } else {
            exportService.exportToCsv(response, "trainers.csv", TRAINER_HEADERS, trainers,
                    trainer -> new String[]{
            String.valueOf(trainer.getId()),
                    trainer.getUser().getUsername(),
                    trainer.getUser().getFirstName(),
                            trainer.getUser().getLastName(),
                            trainer.getUser().getEmail(),
                            trainer.getSpeciality(),
                            trainer.getUser().getAccountStatus().toString()
                    });
        }
    }
}