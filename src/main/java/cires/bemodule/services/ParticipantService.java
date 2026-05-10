package cires.bemodule.services;

import cires.bemodule.dtos2.CreateParticipantRequest;
import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.dtos2.CreateParticipantResponse;
import cires.bemodule.dtos2.PatchParticipantRequest;
import cires.bemodule.entities.Participant;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.exceptions.controllerexceptions.ParticipantNotFoundException;
import cires.bemodule.mappers.ParticipantMapper;
import cires.bemodule.repositories.ParticipantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantService(ParticipantRepository participantRepository, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
    }
    Logger log = LoggerFactory.getLogger(ParticipantService.class);

    // ################################# CREATE ######################################

    public CreateParticipantResponse createParticipant(CreateParticipantRequest request) {
        Participant participant = participantMapper.toParticipant(request);
        participant.setRegistrationSource(RegistrationSource.MANUAL);
        Participant saved = participantRepository.save(participant);

        log.info("Participant created [id={}, email={}]", saved.getId(), saved.getEmail());
        return participantMapper.toCreateParticipantResponse(saved);
    }

    // ################################# READ ########################################

    public ParticipantDTO findParticipantById(Long id) {
        Participant participant = getParticipantIdOrThrow(id);
        return participantMapper.toParticipantDto(participant);
    }

    public List<ParticipantDTO> findAllParticipants() {
        return participantRepository.findAll()
                .stream()
                .map(participantMapper::toParticipantDto)
                .toList();
    }

    // ################################# UPDATE ######################################

    public Participant updateParticipant(Long id,PatchParticipantRequest request) {
        Participant existingParticipant = getParticipantIdOrThrow(id);
        existingParticipant.setFirstName(request.getFirstName());
        existingParticipant.setLastName(request.getLastName());
        existingParticipant.setEmail(request.getEmail());
        existingParticipant.setPhoneNumber(request.getPhoneNumber());
        existingParticipant.setAddress(request.getAddress());
        return participantRepository.save(existingParticipant);
    }

    public ParticipantDTO patchParticipant(Long id, PatchParticipantRequest request) {
        Participant participant = getParticipantIdOrThrow(id);

        participantMapper.patchParticipantFromRequest(request, participant);

        return participantMapper.toParticipantDto(participantRepository.save(participant));

    }

    // ################################# DELETE ######################################

    public void deleteParticipant(Long id) {
        Participant participant = getParticipantIdOrThrow(id);
        participantRepository.delete(participant);
    }

    // ################################# UTILS ######################################

    private Participant getParticipantIdOrThrow(Long id){
        return participantRepository.findById(id).orElseThrow( () -> new ParticipantNotFoundException(id));
    }

}
