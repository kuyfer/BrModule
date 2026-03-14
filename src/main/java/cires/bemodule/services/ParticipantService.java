package cires.bemodule.services;

import cires.bemodule.entities.Participant;
import cires.bemodule.entities.Trainer;
import cires.bemodule.repositories.ParticipantRepository;
import org.springframework.stereotype.Service;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }

    public Participant CreateParticipant(Participant participant) {
       return participantRepository.save(participant);
    }
}
