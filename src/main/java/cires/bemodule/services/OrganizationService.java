package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.entities.Organization;
import cires.bemodule.exceptions.notfound.OrganizationNotFoundException;
import cires.bemodule.exceptions.business.ConflictException;
import cires.bemodule.mappers.OrganizationMapper;
import cires.bemodule.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    // ################################# CREATE ######################################

    @Transactional
    public OrganizationDTO createOrganization(CreateOrganizationRequest request) {
        if (organizationRepository.existsByName(request.getName())) {
            log.warn("Attempt to create organization with duplicate name: {}", request.getName());
            throw new ConflictException("Organization with name '" + request.getName() + "' already exists.");
        }

        Organization organization = organizationMapper.toEntity(request);
        Organization saved = organizationRepository.save(organization);
        log.info("Organization created successfully with id: {} and name: {}", saved.getId(), saved.getName());
        return organizationMapper.toDto(saved);
    }

    // ################################# READ ########################################

    public OrganizationDTO findOrganizationById(Long id) {
        log.debug("Finding organization by id: {}", id);
        Organization organization = getOrganizationOrThrow(id);
        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO findOrganizationByName(String name) {
        log.debug("Finding organization by name: {}", name);
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(() -> {
                    log.warn("Organization not found with name: {}", name);
                    return new OrganizationNotFoundException(name);
                });
        return organizationMapper.toDto(organization);
    }

    public Page<OrganizationDTO> findAll(Pageable pageable, String nameFilter) {
        log.debug("Fetching organizations page - nameFilter: {}, pageable: {}", nameFilter, pageable);
        Page<Organization> page;
        if (nameFilter != null && !nameFilter.isBlank()) {
            page = organizationRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
        } else {
            page = organizationRepository.findAll(pageable);
        }
        log.debug("Found {} organizations (page {} of {})", page.getNumberOfElements(), page.getNumber(), page.getTotalPages());
        return page.map(organizationMapper::toDto);
    }

    // ################################# UPDATE ######################################

    @Transactional
    public OrganizationDTO patchOrganization(Long id, PatchOrganizationRequest request) {
        log.info("Patching organization id={} with request: {}", id, request);
        Organization existing = getOrganizationOrThrow(id);

        // If name is being updated, check uniqueness
        if (request.getName() != null && !existing.getName().equals(request.getName())) {
            if (organizationRepository.existsByName(request.getName())) {
                log.warn("Patch conflict: organization name '{}' already exists", request.getName());
                throw new ConflictException("Organization with name '" + request.getName() + "' already exists.");
            }
            existing.setName(request.getName());
        }

        if (request.getAddress() != null) {
            existing.setAddress(request.getAddress());
        }
        if (request.getContactEmail() != null) {
            existing.setContactEmail(request.getContactEmail());
        }
        if (request.getPhone() != null) {
            existing.setPhone(request.getPhone());
        }

        Organization updated = organizationRepository.save(existing);
        log.info("Organization patched successfully id={}, name={}", updated.getId(), updated.getName());
        return organizationMapper.toDto(updated);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteOrganization(Long id) {
        log.info("Deleting organization with id: {}", id);
        Organization organization = getOrganizationOrThrow(id);
        // Optional: check if it has subsidiaries and decide to throw ConflictException
        // if (organization.getSubsidiaries() != null && !organization.getSubsidiaries().isEmpty()) {
        //     throw new ConflictException("Cannot delete organization with existing subsidiaries.");
        // }
        organizationRepository.delete(organization);
        log.info("Organization deleted successfully with id: {}", id);
    }

    // ################################# UTILS ######################################

    private Organization getOrganizationOrThrow(Long id) {
        log.debug("Looking up organization by id: {}", id);
        return organizationRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Organization not found with id: {}", id);
                    return new OrganizationNotFoundException(id);
                });
    }
}