package cires.bemodule.restcontrollers;

import cires.bemodule.dtos.views.*;
import cires.bemodule.enums.*;
import cires.bemodule.services.*;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/exports")
public class ExportController {

    private final ExportFacadeService exportFacade;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ParticipantService participantService;
    private final TrainingSessionService trainingSessionService;
    private final TrainerService trainerService;

    // Colonnes en français
    private static final String[] NOTIFICATION_HEADERS = {"Id", "Sujet", "Destinataire", "Statut", "Type"};
    private static final String[] PARTICIPANT_HEADERS = {"Id", "Prénom", "Nom", "Email", "Source d'inscription"};
    private static final String[] SESSION_HEADERS = {"Id", "Nom", "Date début", "Date fin", "Statut", "Mode"};
    private static final String[] USER_HEADERS = {"Id", "Nom d'utilisateur", "Prénom", "Nom", "Email", "Rôle", "Statut"};
    private static final String[] TRAINER_HEADERS = {"Id", "Nom d'utilisateur", "Prénom", "Nom", "Email", "Spécialité", "Statut"};

    @GetMapping("/notifications")
    @PreAuthorize("hasAuthority('notification:read')")
    public void exportNotifications(HttpServletResponse response,
                                    @RequestParam(required = false) NotificationType type,
                                    @RequestParam(required = false) NotificationStatus status,
                                    @RequestParam(required = false) String email,
                                    @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<NotificationDTO> data = notificationService.findAll(type, status, email);
        Map<String, String> filters = new LinkedHashMap<>();
        if (type != null)   filters.put("type", type.name());
        if (status != null) filters.put("statut", status.name());
        if (email != null && !email.isBlank()) filters.put("email", email);

        exportFacade.export(response, "notifications", format, filters,
                NOTIFICATION_HEADERS, data,
                n -> new String[]{
                        String.valueOf(n.getId()),
                        n.getSubject(),
                        n.getToEmail(),
                        n.getNotificationStatus().toString(),
                        n.getNotificationType().toString()},
                "Notifications");
    }

    @GetMapping("/participants")
    @PreAuthorize("hasAuthority('participant:read')")
    public void exportParticipants(HttpServletResponse response,
                                   @RequestParam(required = false) RegistrationSource source,
                                   @RequestParam(required = false) Long sessionId,
                                   @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<ParticipantDTO> data = participantService.findAll(source, sessionId);
        Map<String, String> filters = new LinkedHashMap<>();
        if (source != null) filters.put("source", source.name());
        if (sessionId != null) filters.put("session", sessionId.toString());

        exportFacade.export(response, "participants", format, filters,
                PARTICIPANT_HEADERS, data,
                p -> new String[]{
                        String.valueOf(p.getId()),
                        p.getFirstName(),
                        p.getLastName(),
                        p.getEmail(),
                        p.getRegistrationSource().toString()},
                "Participants");
    }

    @GetMapping("/sessions")
    @PreAuthorize("hasAuthority('session:read')")
    public void exportSessions(HttpServletResponse response,
                               @RequestParam(required = false) TrainingSessionStatus status,
                               @RequestParam(required = false) TrainingSessionMode mode,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<TrainingSessionDTO> data = trainingSessionService.findAll(status, mode);
        Map<String, String> filters = new LinkedHashMap<>();
        if (status != null) filters.put("statut", status.name());
        if (mode != null)   filters.put("mode", mode.name());

        exportFacade.export(response, "sessions", format, filters,
                SESSION_HEADERS, data,
                s -> new String[]{
                        String.valueOf(s.getId()),
                        s.getTitle(),
                        s.getStartDate().toString(),
                        s.getEndDate().toString(),
                        s.getStatus().toString(),
                        s.getMode().toString()},
                "Sessions de formation");
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('user:read')")
    public void exportUsers(HttpServletResponse response,
                            @RequestParam(required = false) AccountStatus status,
                            @RequestParam(required = false) String role,
                            @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<UserDTO> data = userService.findAll(role, status);
        Map<String, String> filters = new LinkedHashMap<>();
        if (status != null) filters.put("statut", status.name());
        if (role != null && !role.isBlank()) filters.put("role", role);

        exportFacade.export(response, "utilisateurs", format, filters,
                USER_HEADERS, data,
                u -> new String[]{
                        String.valueOf(u.getId()),
                        u.getUsername(),
                        u.getFirstName(),
                        u.getLastName(),
                        u.getEmail(),
                        u.getRoles().toString(),
                        u.getAccountStatus().toString()},
                "Utilisateurs");
    }

    @GetMapping("/trainers")
    @PreAuthorize("hasAuthority('trainer:read')")
    public void exportTrainers(HttpServletResponse response,
                               @RequestParam(required = false) String speciality,
                               @RequestParam(defaultValue = "CSV") ExportFormat format) throws IOException {
        List<TrainerDTO> data = trainerService.findAll(speciality);
        Map<String, String> filters = new LinkedHashMap<>();
        if (speciality != null && !speciality.isBlank()) filters.put("spécialité", speciality);

        exportFacade.export(response, "formateurs", format, filters,
                TRAINER_HEADERS, data,
                t -> new String[]{
                        String.valueOf(t.getId()),
                        t.getUser().getUsername(),
                        t.getUser().getFirstName(),
                        t.getUser().getLastName(),
                        t.getUser().getEmail(),
                        t.getSpeciality(),
                        t.getUser().getAccountStatus().toString()},
                "Formateurs");
    }
}