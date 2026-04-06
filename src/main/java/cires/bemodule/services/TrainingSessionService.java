package cires.bemodule.services;

import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.mappers.TrainingSessionMapper;
import cires.bemodule.repositories.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
//    private final TrainerService trainerService;
//    private final ParticipantService participantService;
    private final TrainingSessionMapper trainingSessionMapper;

    public TrainingSession createTrainingSession(TrainingSession trainingSession) {
        return trainingSessionRepository.save(trainingSession);
    }

    public TrainingSessionDTO getTrainingSessionById(Long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id).orElseThrow( () -> new RuntimeException("Training session not found with id " + id));
        return trainingSessionMapper.toDto(trainingSession);
    }
}
