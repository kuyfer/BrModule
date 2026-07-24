package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateParticipantRequest;
import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.responses.CreateParticipantResponse;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.entities.Participant;
import cires.bemodule.enums.RegistrationSource;
import cires.bemodule.exceptions.notfound.ParticipantNotFoundException;
import cires.bemodule.mappers.ParticipantMapper;
import cires.bemodule.repositories.ParticipantRepository;
import cires.bemodule.specifications.ParticipantsSpecifications;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final ParticipantMapper participantMapper;

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

    public Page<ParticipantDTO> findAll(RegistrationSource source, Pageable pageable) {
        log.debug("Fetching participants with source: {}, pageable: {}", source, pageable);
        Specification<Participant> spec = Specification
                .where(ParticipantsSpecifications.hasRegistration(source));
        Page<Participant> participantPage = participantRepository.findAll(spec, pageable);
        return participantPage.map(participantMapper::toParticipantDto);
    }

    public List<ParticipantDTO> findAll(RegistrationSource source) {
        log.info("Finding all participants (unpaged) with source: {}", source);
        return findAll(source, Pageable.unpaged()).getContent();
    }

    // ################################# UPDATE ######################################

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