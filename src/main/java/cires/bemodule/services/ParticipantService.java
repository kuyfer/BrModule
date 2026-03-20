package cires.bemodule.services;

import cires.bemodule.dtos.CreateParticipantRequest;
import cires.bemodule.entities.Participant;
import cires.bemodule.repositories.ParticipantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public ParticipantService(ParticipantRepository participantRepository) {this.participantRepository = participantRepository;}

    public Participant createParticipant(CreateParticipantRequest request) {

        Participant participant = new Participant();
        participant.setFirstName(request.getFirstName());
        participant.setLastName(request.getLastName());
        participant.setEmail(request.getEmail());
        participant.setPhone(request.getPhoneNumber());
        participant.setAddress(request.getAddress());
        participant.setRegistrationSource(request.getRegistrationSource());

        return participantRepository.save(participant);
    }

    public Participant getParticipantById(Long id) {
        return participantRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Participant not found with id " + id));
    }

    public List<Participant> allParticipants() {
        return participantRepository.findAll();
    }

}
