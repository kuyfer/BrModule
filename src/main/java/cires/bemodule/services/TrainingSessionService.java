package cires.bemodule.services;

import cires.bemodule.dtos.requests.CancelTrainingSessionRequest;
import cires.bemodule.dtos.responses.CancelTrainingSessionResponse;
import cires.bemodule.dtos.requests.CreateTrainingSessionRequest;
import cires.bemodule.dtos.views.TrainingSessionDTO;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.enums.TrainingSessionMode;
import cires.bemodule.enums.TrainingSessionStatus;
import cires.bemodule.exceptions.controllerexceptions.TrainerNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.TrainingSessionNotFoundException;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.mappers.TrainingSessionMapper;
import cires.bemodule.repositories.ParticipantRepository;
import cires.bemodule.repositories.TrainerRepository;
import cires.bemodule.repositories.TrainingSessionRepository;
import cires.bemodule.specifications.TrainingSessionSpecifications;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;

import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final TrainerRepository trainerRepository;
    private final ParticipantRepository participantRepository;
    private final NotificationService notificationService;

    // ################################# CREATE ######################################

    public TrainingSessionDTO createTrainingSession(CreateTrainingSessionRequest request) {
        log.info("Creating training session with title: {}, trainerId: {}", request.getTitle(), request.getTrainerId());
        Trainer trainer = trainerRepository.findById(request.getTrainerId())
                .orElseThrow(() -> {
                    log.error("Trainer not found with id: {}", request.getTrainerId());
                    return new TrainerNotFoundException(request.getTrainerId());
                });

        TrainingSession session = new TrainingSession();
        session.setStartDate(request.getStartDate());
        session.setEndDate(request.getEndDate());
        session.setTitle(request.getTitle());
        session.setStatus(TrainingSessionStatus.SCHEDULED);
        session.setLocation(request.getLocation());
        session.setMode(request.getMode());
        session.setDescription(request.getDescription());
        session.setTrainer(trainer);

        notificationService.sendTrainerAssignmentEmail(session, trainer);

        TrainingSession savedSession = trainingSessionRepository.save(session);
        log.info("Training session created with id: {}", savedSession.getId());

        return trainingSessionMapper.toTrainingSessionDto(savedSession);
    }

    public void addParticipants() {
        log.debug("Add participants method called with default id 1L");
        TrainingSession session = getSessionIdOrThrow(1L);
        // method incomplete in original
    }

    public void addParticipants(Long trainingSessionId, List<Long> participantIds) {
        log.info("Adding participants {} to training session id: {}", participantIds, trainingSessionId);
        TrainingSession trainingSession = getSessionIdOrThrow(trainingSessionId);
        List<Participant> participants = participantRepository.findAllById(participantIds);
        log.info("Found {} participants to add to session id: {}", participants.size(), trainingSessionId);
        // remaining logic not provided in original
    }

    // ################################# READ ######################################

    public TrainingSessionDTO findTrainingSessionById(Long id) {
        log.info("Finding training session by id: {}", id);
        TrainingSession trainingSession = getSessionIdOrThrow(id);
        TrainingSessionDTO dto = trainingSessionMapper.toTrainingSessionDto(trainingSession);
        log.info("Found training session with id: {}", id);
        return dto;
    }

    public Page<TrainingSessionDTO> findAll(TrainingSessionStatus status, TrainingSessionMode mode, Pageable pageable) {
        Specification<TrainingSession> spec = Specification
                .where(TrainingSessionSpecifications.hasMode(mode))
                .and(TrainingSessionSpecifications.hasStatus(status));
        Page<TrainingSession> sessionPage = trainingSessionRepository.findAll(spec, pageable);

        Page<TrainingSessionDTO> dtoPage = sessionPage.map(trainingSessionMapper::toTrainingSessionDto);
        return dtoPage;
    }

    public List<TrainingSessionDTO> findAll(TrainingSessionStatus status, TrainingSessionMode mode) {
        Page<TrainingSessionDTO> page = findAll(status, mode, Pageable.unpaged());
        return page.getContent();
    }
    // ################################# UPDATE ######################################

    public CancelTrainingSessionResponse cancelSession(Long id, CancelTrainingSessionRequest request) {
        log.info("Cancelling training session id: {}, reason: {}", id, request.getReason());
        TrainingSession session = getSessionIdOrThrow(id);

        session.setStatus(TrainingSessionStatus.CANCELLED);
        trainingSessionRepository.save(session);

        notificationService.sendSessionCancelledEmail(session, request.getReason());
        log.info("Training session cancelled id: {}", id);
        return trainingSessionMapper.toCancelTrainingSessionResponse(session);
    }

    public void changeStatus(Long id, TrainingSessionStatus newStatus) {
        log.info("Changing status of training session id: {} to {}", id, newStatus);
        TrainingSession session = getSessionIdOrThrow(id);
        assertValidTransition(session.getStatus(), newStatus);

        session.setStatus(newStatus);
        trainingSessionRepository.save(session);
        log.info("Training session id: {} status changed to {}", id, newStatus);
    }

    // ################################# DELETE ######################################

    public void deleteTrainingSession(Long id) {
        log.info("Deleting training session id: {}", id);
        TrainingSession session = trainingSessionRepository.findById(id).orElseThrow(() -> {
                    log.error("training session not found for deletion with id: {}", id);
                    return new TrainingSessionNotFoundException(id);
        });
        trainingSessionRepository.delete(session);
        log.info("Training session deleted id: {}", id);
    }

    // ################################# UTILS ######################################

    private TrainingSession getSessionIdOrThrow(Long id) {
        log.debug("Looking up training session by id: {}", id);
        return trainingSessionRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Training session not found with id: {}", id);
                    return new TrainingSessionNotFoundException(id);
                });
    }

    private void assertValidTransition(TrainingSessionStatus current, TrainingSessionStatus next) {
        log.debug("Validating status transition from {} to {}", current, next);
        boolean valid = switch (current) {
            case SCHEDULED, POSTPONED -> next == TrainingSessionStatus.ONGOING ||
                    next == TrainingSessionStatus.CANCELLED ||
                    next == TrainingSessionStatus.POSTPONED;

            case ONGOING -> next == TrainingSessionStatus.COMPLETED ||
                    next == TrainingSessionStatus.CANCELLED;

            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            log.warn("Invalid status transition attempted: from {} to {}", current, next);
            throw new ConflictException(
                    "Cannot transition session from " + current + " to " + next
            );
        }
    }
}