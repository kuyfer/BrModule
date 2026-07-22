package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateSubsidiaryRequest;
import cires.bemodule.dtos.requests.PatchSubsidiaryRequest;
import cires.bemodule.dtos.views.SubsidiaryDTO;
import cires.bemodule.entities.Organization;
import cires.bemodule.entities.Subsidiary;
import cires.bemodule.exceptions.notfound.OrganizationNotFoundException;
import cires.bemodule.exceptions.notfound.SubsidiaryNotFoundException;
import cires.bemodule.exceptions.business.ConflictException;
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
        if (subsidiaryRepository.existsByName(request.getName())) {
            log.warn("Attempt to create subsidiary with duplicate name: {}", request.getName());
            throw new ConflictException("Subsidiary with name '" + request.getName() + "' already exists.");
        }

        Organization organization = null;
        if (request.getOrganizationId() != null) {
            organization = organizationRepository.findById(request.getOrganizationId())
                    .orElseThrow(() -> {
                        log.error("Organization not found with id: {}", request.getOrganizationId());
                        return new OrganizationNotFoundException(request.getOrganizationId());
                    });
        }

        Subsidiary subsidiary = subsidiaryMapper.toSubsidiary(request);
        subsidiary.setOrganization(organization);

        Subsidiary saved = subsidiaryRepository.save(subsidiary);
        log.info("Subsidiary created successfully with id: {} and name: {}", saved.getId(), saved.getName());
        return subsidiaryMapper.toSubsidiaryDto(saved);
    }

    // ################################# READ ########################################

    public SubsidiaryDTO findSubsidiaryById(Long id) {
        log.debug("Finding subsidiary by id: {}", id);
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        return subsidiaryMapper.toSubsidiaryDto(subsidiary);
    }

    public SubsidiaryDTO findSubsidiaryByName(String name) {
        log.debug("Finding subsidiary by name: {}", name);
        Subsidiary subsidiary = subsidiaryRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Subsidiary not found with name: {}", name);
                    return new SubsidiaryNotFoundException(name);
                });
        return subsidiaryMapper.toSubsidiaryDto(subsidiary);
    }

    public Page<SubsidiaryDTO> findAll(Pageable pageable, String nameFilter) {
        log.debug("Fetching subsidiaries page - nameFilter: {}, pageable: {}", nameFilter, pageable);
        Page<Subsidiary> page;
        if (nameFilter != null && !nameFilter.isBlank()) {
            page = subsidiaryRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
        } else {
            page = subsidiaryRepository.findAll(pageable);
        }
        log.debug("Found {} subsidiaries (page {} of {})", page.getNumberOfElements(), page.getNumber(), page.getTotalPages());
        return page.map(subsidiaryMapper::toSubsidiaryDto);
    }

    public List<SubsidiaryDTO> findAll(String nameFilter) {
        log.debug("Fetching all subsidiaries (unpaged) - nameFilter: {}", nameFilter);
        Page<SubsidiaryDTO> page = findAll(Pageable.unpaged(), nameFilter);
        return page.getContent();
    }

    // ################################# UPDATE ######################################

    public SubsidiaryDTO patchSubsidiary(Long id, PatchSubsidiaryRequest request) {
        log.info("Patching subsidiary id={} with request: {}", id, request);
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        subsidiaryMapper.patchSubsidiaryFromRequest(request, subsidiary);
        Subsidiary saved = subsidiaryRepository.save(subsidiary);
        log.info("Subsidiary patched successfully id={}, name={}", saved.getId(), saved.getName());
        return subsidiaryMapper.toSubsidiaryDto(saved);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteSubsidiary(Long id) {
        log.info("Deleting subsidiary with id: {}", id);
        Subsidiary subsidiary = getSubsidiaryOrThrow(id);
        subsidiaryRepository.delete(subsidiary);
        log.info("Subsidiary deleted successfully with id: {}", id);
    }

    // ################################# UTILS ######################################

    private Subsidiary getSubsidiaryOrThrow(Long id) {
        log.debug("Looking up subsidiary by id: {}", id);
        return subsidiaryRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Subsidiary not found with id: {}", id);
                    return new SubsidiaryNotFoundException(id);
                });
    }
}