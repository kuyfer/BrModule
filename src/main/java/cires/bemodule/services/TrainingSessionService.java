package cires.bemodule.services;

import cires.bemodule.dtos2.CancelTrainingSessionRequest;
import cires.bemodule.dtos2.CancelTrainingSessionResponse;
import cires.bemodule.dtos2.CreateTrainingSessionRequest;
import cires.bemodule.dtos2.CreateTrainingSessionResponse;
import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.NotificationType;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.exceptions.controllerexceptions.TrainerNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.TrainingSessionNotFoundException;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.mappers.TrainingSessionMapper;
import cires.bemodule.models.EmailPayload;
import cires.bemodule.repositories.ParticipantRepository;
import cires.bemodule.repositories.TrainerRepository;
import cires.bemodule.repositories.TrainingSessionRepository;
import cires.bemodule.specifications.TrainingSessionSpecifications;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TrainingSessionService {

    private static final Logger logger = LoggerFactory.getLogger(TrainingSessionService.class);

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final TrainerRepository trainerRepository;
    private final ParticipantRepository participantRepository;
    private final EmailQueueProducer emailQueueProducer;

    // ################################# CREATE ######################################

    public CreateTrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request) {
        logger.info("Creating training session with title: {}, trainerId: {}", request.getTitle(), request.getTrainerId());
        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> {
                    logger.error("Trainer not found with id: {}", request.getTrainerId());
                    return new TrainerNotFoundException(request.getTrainerId());
                });

        TrainingSession session = new TrainingSession();
        session.setStartDate(request.getStartDate());
        session.setEndDate(request.getEndDate());
        session.setTitle(request.getTitle());
        // session.setSubsidiary(request.getSubsidiary());
        session.setStatus(TrainingSessionStatus.SCHEDULED);
        session.setLocation(request.getLocation());
        session.setMode(request.getMode());
        session.setDescription(request.getDescription());
        session.setTrainer(trainer);

        sendTrainerAssignmentEmail(session, trainer);

        trainingSessionRepository.save(session);
        logger.info("Training session created with id: {}", session.getId());

        return new CreateTrainingSessionResponse("done");
    }

    public void addParticipants() {
        logger.debug("Add participants method called with default id 1L");
        TrainingSession session = getSessionIdOrThrow(1L);
        // method incomplete in original
    }

    public void addParticipants(Long trainingSessionId, List<Long> participantIds) {
        logger.info("Adding participants {} to training session id: {}", participantIds, trainingSessionId);
        TrainingSession trainingSession = getSessionIdOrThrow(trainingSessionId);
        List<Participant> participants = participantRepository.findAllById(participantIds);
        logger.info("Found {} participants to add to session id: {}", participants.size(), trainingSessionId);
        // remaining logic not provided in original
    }

    // ################################# READ ######################################

    public TrainingSessionDTO findTrainingSessionById(Long id) {
        logger.info("Finding training session by id: {}", id);
        TrainingSession trainingSession = getSessionIdOrThrow(id);
        TrainingSessionDTO dto = trainingSessionMapper.toTrainingSessionDto(trainingSession);
        logger.info("Found training session with id: {}", id);
        return dto;
    }

    public List<TrainingSessionDTO> findAll(TrainingSessionStatus status, TrainingSessionMode mode) {
        logger.info("Finding all training sessions with filters - status: {}, mode: {}", status, mode);
        Specification<TrainingSession> spec = Specification
                .where(TrainingSessionSpecifications.hasMode(mode))
                .and(TrainingSessionSpecifications.hasStatus(status));
        List<TrainingSession> sessions = trainingSessionRepository.findAll(spec);
        List<TrainingSessionDTO> dtos = sessions.stream()
                .map(trainingSessionMapper::toTrainingSessionDto)
                .toList();
        logger.info("Found {} training sessions matching filters", dtos.size());
        return dtos;
    }

    // ################################# UPDATE ######################################

    public CancelTrainingSessionResponse cancelSession(Long id, CancelTrainingSessionRequest request) {
        logger.info("Cancelling training session id: {}, reason: {}", id, request.getReason());
        TrainingSession session = getSessionIdOrThrow(id);

        session.setStatus(TrainingSessionStatus.CANCELLED);
        trainingSessionRepository.save(session);

        sendSessionCancelledEmail(session, request.getReason());
        logger.info("Training session cancelled id: {}", id);
        return trainingSessionMapper.toCancelTrainingSessionResponse(session);
    }

    public void changeStatus(Long id, TrainingSessionStatus newStatus) {
        logger.info("Changing status of training session id: {} to {}", id, newStatus);
        TrainingSession session = getSessionIdOrThrow(id);
        assertValidTransition(session.getStatus(), newStatus);

        session.setStatus(newStatus);
        trainingSessionRepository.save(session);
        logger.info("Training session id: {} status changed to {}", id, newStatus);
    }

    // ################################# DELETE ######################################

    public void deleteTrainingSession(Long id) {
        logger.info("Deleting training session id: {}", id);
        trainingSessionRepository.deleteById(id);
        logger.info("Training session deleted id: {}", id);
    }

    // ################################# UTILS ######################################

    private TrainingSession getSessionIdOrThrow(Long id) {
        logger.debug("Looking up training session by id: {}", id);
        return trainingSessionRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Training session not found with id: {}", id);
                    return new TrainingSessionNotFoundException(id);
                });
    }

    private void sendTrainerAssignmentEmail(TrainingSession session, Trainer trainer) {
        logger.debug("Sending trainer assignment email to trainer: {} for session: {}", trainer.getUser().getEmail(), session.getTitle());
        Map<String, Object> model = new HashMap<>();
        model.put("trainerName", trainer.getUser().getFirstName());
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        //model.put("subsidiary", session.getSubsidiary());

        EmailPayload payload = new EmailPayload(
                trainer.getUser().getEmail(),
                "Trainer Assignment",
                "trainer-assignement",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.TRAINER_ASSIGNMENT);
        logger.debug("Trainer assignment email queued for: {}", trainer.getUser().getEmail());
    }

    private void sendSessionCancelledEmail(TrainingSession session, String reason) {
        logger.debug("Sending session cancellation email to trainer: {} for session: {}", session.getTrainer().getUser().getEmail(), session.getTitle());
        Map<String, Object> model = new HashMap<>();
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        model.put("cancellationReason", reason);

        EmailPayload payload = new EmailPayload(
                session.getTrainer().getUser().getEmail(),
                "Session Cancelled",
                "session-cancellation",
                model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.SESSION_CANCELLATION);
        logger.debug("Session cancellation email queued for: {}", session.getTrainer().getUser().getEmail());
    }

    private void assertValidTransition(TrainingSessionStatus current, TrainingSessionStatus next) {
        logger.debug("Validating status transition from {} to {}", current, next);
        boolean valid = switch (current) {
            case SCHEDULED, POSTPONED -> next == TrainingSessionStatus.ONGOING ||
                    next == TrainingSessionStatus.CANCELLED ||
                    next == TrainingSessionStatus.POSTPONED;

            case ONGOING -> next == TrainingSessionStatus.COMPLETED ||
                    next == TrainingSessionStatus.CANCELLED;

            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            logger.warn("Invalid status transition attempted: from {} to {}", current, next);
            throw new ConflictException(
                    "Cannot transition session from " + current + " to " + next
            );
        }
    }
}