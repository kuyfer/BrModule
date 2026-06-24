package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateSubsidiaryRequest;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.dtos.requests.PatchSubsidiaryRequest;
import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.views.SubsidiaryDTO;
import cires.bemodule.entities.Organization;
import cires.bemodule.entities.Participant;
import cires.bemodule.entities.Subsidiary;
import cires.bemodule.exceptions.controllerexceptions.OrganizationNotFoundException;
import cires.bemodule.exceptions.controllerexceptions.SubsidiaryNotFoundException;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.mappers.SubsidiaryMapper;
import cires.bemodule.repositories.OrganizationRepository;
import cires.bemodule.repositories.SubsidiaryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubsidiaryService {

    private final SubsidiaryRepository subsidiaryRepository;
    private final OrganizationRepository organizationRepository;
    private final SubsidiaryMapper subsidiaryMapper;

    // ################################# CREATE ######################################

    @Transactional
    public SubsidiaryDTO createSubsidiary(CreateSubsidiaryRequest request) {
        // Check if name already exists
        if (subsidiaryRepository.existsByName(request.getName())) {
            throw new ConflictException("Subsidiary with name '" + request.getName() + "' already exists.");
        }

        // Resolve organization if provided
        Organization organization = null;
        if (request.getOrganizationId() != null) {
            organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> new OrganizationNotFoundException(request.getOrganizationId()));
        }

        Subsidiary subsidiary = subsidiaryMapper.toSubsidiary(request);
        subsidiary.setOrganization(organization);

        Subsidiary saved = subsidiaryRepository.save(subsidiary);
        return subsidiaryMapper.toSubsidiaryDto(saved);
    }

    // ################################# READ ########################################

    public SubsidiaryDTO findSubsidiaryById(Long id) {
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        return subsidiaryMapper.toSubsidiaryDto(subsidiary);
    }

    public SubsidiaryDTO findSubsidiaryByName(String name) {
        Subsidiary subsidiary = subsidiaryRepository.findByName(name)
                .orElseThrow(() -> new SubsidiaryNotFoundException(name));
        return subsidiaryMapper.toSubsidiaryDto(subsidiary);
    }

    public Page<SubsidiaryDTO> findAll(Pageable pageable, String nameFilter) {
        Page<Subsidiary> page;
        if (nameFilter != null && !nameFilter.isBlank()) {
            page = subsidiaryRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
        } else {
            page = subsidiaryRepository.findAll(pageable);
        }
        return page.map(subsidiaryMapper::toSubsidiaryDto);
    }

    // Convenience: unpaged list (if needed)
    public List<SubsidiaryDTO> findAll(String nameFilter) {
        Page<SubsidiaryDTO> page = findAll(Pageable.unpaged(), nameFilter);
        return page.getContent();
    }

    // ################################# UPDATE ######################################

    public SubsidiaryDTO patchSubsidiary(Long id, PatchSubsidiaryRequest request) {
        log.info("Patching subsidiary id={} with request: {}", id, request);
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        subsidiaryMapper.patchSubsidiaryFromRequest(request, subsidiary);
        Subsidiary saved = subsidiaryRepository.save(subsidiary);
        return subsidiaryMapper.toSubsidiaryDto(saved);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteSubsidiary(Long id) {
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        subsidiaryRepository.delete(subsidiary);
    }

    // ################################# UTILS ######################################

    private Subsidiary getSubsidiaryOrThrow(Long id) {
        return subsidiaryRepository.findById(id)
                .orElseThrow(() -> new SubsidiaryNotFoundException(id));
    }
}