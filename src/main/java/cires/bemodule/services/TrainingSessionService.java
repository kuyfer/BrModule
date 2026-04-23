package cires.bemodule.services;

import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.TrainingSession;
import cires.bemodule.exceptions.controllerexceptions.TrainingSessionNotFoundException;
import cires.bemodule.mappers.TrainingSessionMapper;
import cires.bemodule.repositories.ParticipantRepository;
import cires.bemodule.repositories.TrainingSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service @RequiredArgsConstructor
public class TrainingSessionService {

    private final TrainingSessionRepository trainingSessionRepository;
    private final TrainingSessionMapper trainingSessionMapper;
    private final ParticipantRepository participantRepository;

    public TrainingSession createTrainingSession(TrainingSession trainingSession) {
        return trainingSessionRepository.save(trainingSession);
    }

    public TrainingSessionDTO getTrainingSessionById(Long id) {
        TrainingSession trainingSession = getSessionIdOrThrow(id);
        return trainingSessionMapper.toDto(trainingSession);
    }
    // TODO : add filters maybe
    public List<TrainingSessionDTO> findALl(){
        return trainingSessionRepository.findAll().stream().map(trainingSessionMapper::toDto).toList();
    }
    public void deleteTrainingSession(Long id) {
        trainingSessionRepository.deleteById(id);
    }

    public void addParticipants(Long trainingSessionId, List<Long> participantIds) {
        TrainingSession trainingSession = getSessionIdOrThrow(trainingSessionId);
        List<Participant> participants = participantRepository.findAllById(participantIds);
    }

    private TrainingSession getSessionIdOrThrow(Long id){
        return trainingSessionRepository.findById(id).orElseThrow( () -> new TrainingSessionNotFoundException(id));
    }
}
