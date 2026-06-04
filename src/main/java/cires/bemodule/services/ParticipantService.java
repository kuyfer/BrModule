package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateParticipantRequest;
import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.responses.CreateParticipantResponse;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.entities.Participant;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.exceptions.controllerexceptions.ParticipantNotFoundException;
import cires.bemodule.mappers.ParticipantMapper;
import cires.bemodule.repositories.ParticipantRepository;
import cires.bemodule.specifications.ParticipantsSpecifications;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParticipantService {

    private static final Logger log = LoggerFactory.getLogger(ParticipantService.class);

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

    public ParticipantService(ParticipantRepository participantRepository, ParticipantMapper participantMapper) {
        this.participantRepository = participantRepository;
        this.participantMapper = participantMapper;
    }

    // ################################# CREATE ######################################

    public CreateParticipantResponse createParticipant(CreateParticipantRequest request) {
        log.info("Creating participant with email: {}", request.getEmail());
        Participant participant = participantMapper.toParticipant(request);
        participant.setRegistrationSource(RegistrationSource.MANUAL);
        Participant saved = participantRepository.save(participant);

        log.info("Participant created [id={}, email={}]", saved.getId(), saved.getEmail());
        return participantMapper.toCreateParticipantResponse(saved);
    }

    // ################################# READ ########################################

    public ParticipantDTO findParticipantById(Long id) {
        log.info("Finding participant by id: {}", id);
        Participant participant = getParticipantIdOrThrow(id);
        ParticipantDTO dto = participantMapper.toParticipantDto(participant);
        log.info("Found participant with id: {}", id);
        return dto;
    }

    public List<ParticipantDTO> findAll(RegistrationSource source) {
        log.info("Finding all participants with registration source: {}", source);
        Specification<Participant> spec = Specification
                .where(ParticipantsSpecifications.hasRegistration(source));
        List<ParticipantDTO> result = participantRepository.findAll(spec)
                .stream()
                .map(participantMapper::toParticipantDto)
                .toList();
        log.info("Found {} participants for source: {}", result.size(), source);
        return result;
    }

    // ################################# UPDATE ######################################

    public Participant updateParticipant(Long id, PatchParticipantRequest request) {
        log.info("Updating participant id={} with request: {}", id, request);
        Participant existingParticipant = getParticipantIdOrThrow(id);
        existingParticipant.setFirstName(request.getFirstName());
        existingParticipant.setLastName(request.getLastName());
        existingParticipant.setEmail(request.getEmail());
        existingParticipant.setPhoneNumber(request.getPhoneNumber());
        existingParticipant.setAddress(request.getAddress());
        Participant updated = participantRepository.save(existingParticipant);
        log.info("Participant updated id={}, email={}", updated.getId(), updated.getEmail());
        return updated;
    }

    public ParticipantDTO patchParticipant(Long id, PatchParticipantRequest request) {
        log.info("Patching participant id={} with request: {}", id, request);
        Participant participant = getParticipantIdOrThrow(id);
        participantMapper.patchParticipantFromRequest(request, participant);
        Participant saved = participantRepository.save(participant);
        log.info("Participant patched id={}, email={}", saved.getId(), saved.getEmail());
        return participantMapper.toParticipantDto(saved);
    }

    // ################################# DELETE ######################################

    public void deleteParticipant(Long id) {
        log.info("Deleting participant id={}", id);
        Participant participant = getParticipantIdOrThrow(id);
        participantRepository.delete(participant);
        log.info("Participant deleted id={}", id);
    }

    // ################################# UTILS ######################################

    private Participant getParticipantIdOrThrow(Long id) {
        log.debug("Looking up participant by id: {}", id);
        return participantRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Participant not found with id: {}", id);
                    return new ParticipantNotFoundException(id);
                });
    }
}