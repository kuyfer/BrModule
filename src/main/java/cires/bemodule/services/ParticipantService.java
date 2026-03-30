package cires.bemodule.services;

import cires.bemodule.dtos.CreateParticipantRequest;
import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.dtos.UserDTO;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.User;
import cires.bemodule.mappers.ParticipantMapper;
import cires.bemodule.repositories.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

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

    public ParticipantDTO getParticipantById(Long id) {
        Participant participant = participantRepository.findById(id).orElseThrow(() -> new RuntimeException("Participant not found with id " + id));
        return participantMapper.toParticipantDto(participant);
    }

    public List<ParticipantDTO> allParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(participantMapper::toParticipantDto)
                .collect(Collectors.toList());
    }

}
