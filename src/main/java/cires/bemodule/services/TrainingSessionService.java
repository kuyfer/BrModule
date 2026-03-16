package cires.bemodule.services;

import cires.bemodule.entities.TrainingSession;
import cires.bemodule.repositories.TrainingSessionRepository;
import org.springframework.stereotype.Service;

@Service
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainerService trainerService;
    private final ParticipantService participantService;

    public TrainingSessionService(TrainingSessionRepository trainingSessionRepository, TrainerService trainerService, ParticipantService participantService) {
        this.trainingSessionRepository = trainingSessionRepository;
        this.trainerService = trainerService;
        this.participantService = participantService;

    }
    public TrainingSession CreateTrainingSession(TrainingSession trainingSession) {
        return trainingSessionRepository.save(trainingSession);
    }
}
