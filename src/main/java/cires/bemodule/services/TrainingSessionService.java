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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service @RequiredArgsConstructor
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final TrainerRepository trainerRepository;
    private final ParticipantRepository participantRepository;
    private final EmailQueueProducer emailQueueProducer;


    // ################################# CREATE ######################################

    public CreateTrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request) {
        Trainer trainer = trainerRepository.findById(request.getTrainerId()).orElseThrow(()-> new TrainerNotFoundException(request.getTrainerId()));

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

        return new CreateTrainingSessionResponse("done");
    }
    public void addParticipants(){
        TrainingSession session = getSessionIdOrThrow(1L);
    }

    public void addParticipants(Long trainingSessionId, List<Long> participantIds) {
        TrainingSession trainingSession = getSessionIdOrThrow(trainingSessionId);
        List<Participant> participants = participantRepository.findAllById(participantIds);
    }
    // ################################# READ ######################################

    public TrainingSessionDTO findTrainingSessionById(Long id) {
        TrainingSession trainingSession = getSessionIdOrThrow(id);
        return trainingSessionMapper.toTrainingSessionDto(trainingSession);
    }

    public List<TrainingSessionDTO> findAll(TrainingSessionStatus status, TrainingSessionMode mode){
        Specification<TrainingSession> spec = Specification
                .where(TrainingSessionSpecifications.hasMode(mode))
                .and(TrainingSessionSpecifications.hasStatus(status));
        List<TrainingSession> sessions = trainingSessionRepository.findAll(spec);
        return sessions.stream()
                .map(trainingSessionMapper::toTrainingSessionDto)
                .toList();
    }

    // ################################# UPDATE ######################################

    public CancelTrainingSessionResponse cancelSession(Long id, CancelTrainingSessionRequest request){
        TrainingSession session = getSessionIdOrThrow(id);

        session.setStatus(TrainingSessionStatus.CANCELLED);
        trainingSessionRepository.save(session);

        sendSessionCancelledEmail(session, request.getReason());
        return trainingSessionMapper.toCancelTrainingSessionResponse(session);
    }

    public void changeStatus(Long id, TrainingSessionStatus newStatus) {
        TrainingSession session = getSessionIdOrThrow(id);
        assertValidTransition(session.getStatus(), newStatus);

        session.setStatus(newStatus);
        trainingSessionRepository.save(session);

    }

    // ################################# DELETE ######################################

    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }

    // ################################# UTILS ######################################

    private TrainingSession getSessionIdOrThrow(Long id){
        return trainingSessionRepository.findById(id).orElseThrow( () -> new TrainingSessionNotFoundException(id));
    }

    private void sendTrainerAssignmentEmail(TrainingSession session, Trainer trainer) {
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

    }

    private void sendSessionCancelledEmail(TrainingSession session, String reason){
        Map<String, Object> model = new HashMap<>();
        model.put("sessionTitle", session.getTitle());
        model.put("sessionDescription", session.getDescription());
        model.put("startDate", session.getStartDate().toString());
        model.put("endDate", session.getEndDate().toString());
        model.put("location", session.getLocation());
        model.put("mode", session.getMode());
        model.put("cancellationReason", reason );


        EmailPayload payload = new EmailPayload(
               session.getTrainer().getUser().getEmail(),
               "Session Cancelled",
               "session-cancellation",
               model
        );

        emailQueueProducer.queueEmail(payload, NotificationType.SESSION_CANCELLATION);
    }

    private void assertValidTransition(TrainingSessionStatus current, TrainingSessionStatus next) {
        boolean valid = switch (current) {
            case SCHEDULED, POSTPONED -> next == TrainingSessionStatus.ONGOING  ||
                    next == TrainingSessionStatus.CANCELLED ||
                    next == TrainingSessionStatus.POSTPONED;

            case ONGOING   -> next == TrainingSessionStatus.COMPLETED ||
                    next == TrainingSessionStatus.CANCELLED ;


            case COMPLETED, CANCELLED -> false;
        };

        if (!valid) {
            throw new ConflictException(
                    "Cannot transition session from " + current + " to " + next
            );
        }
    }

}
