package cires.bemodule.services;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.entities.Organization;
import cires.bemodule.exceptions.controllerexceptions.OrganizationNotFoundException;
import cires.bemodule.exceptions.validationexceptions.ConflictException;
import cires.bemodule.mappers.OrganizationMapper;
import cires.bemodule.repositories.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;
    private final OrganizationMapper organizationMapper;

    // ################################# CREATE ######################################

    @Transactional
    public OrganizationDTO createOrganization(CreateOrganizationRequest request) {
        if (organizationRepository.existsByName(request.getName())) {
            throw new ConflictException("Organization with name '" + request.getName() + "' already exists.");
        }

        Organization organization = organizationMapper.toEntity(request);
        Organization saved = organizationRepository.save(organization);
        return organizationMapper.toDto(saved);
    }

    // ################################# READ ########################################

    public OrganizationDTO findOrganizationById(Long id) {
        Organization organization = getOrganizationOrThrow(id);
        return organizationMapper.toDto(organization);
    }

    public OrganizationDTO findOrganizationByName(String name) {
        Organization organization = organizationRepository.findByName(name)
                .orElseThrow(() -> new OrganizationNotFoundException(name));
        return organizationMapper.toDto(organization);
    }

    public Page<OrganizationDTO> findAll(Pageable pageable, String nameFilter) {
        Page<Organization> page;
        if (nameFilter != null && !nameFilter.isBlank()) {
            page = organizationRepository.findByNameContainingIgnoreCase(nameFilter, pageable);
        } else {
            page = organizationRepository.findAll(pageable);
        }
        return page.map(organizationMapper::toDto);
    }

    // ################################# UPDATE ######################################

    @Transactional
    public OrganizationDTO patchOrganization(Long id, PatchOrganizationRequest request) {
        Organization existing = getOrganizationOrThrow(id);

        // If name is being updated, check uniqueness
        if (request.getName() != null && !existing.getName().equals(request.getName())) {
            if (organizationRepository.existsByName(request.getName())) {
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
        return organizationMapper.toDto(updated);
    }

    // ################################# DELETE ######################################

    @Transactional
    public void deleteOrganization(Long id) {
        Organization organization = getOrganizationOrThrow(id);
        // Optional: check if it has subsidiaries and decide to throw ConflictException
        // if (organization.getSubsidiaries() != null && !organization.getSubsidiaries().isEmpty()) {
        //     throw new ConflictException("Cannot delete organization with existing subsidiaries.");
        // }
        organizationRepository.delete(organization);
    }

    // ################################# UTILS ######################################

    private Organization getOrganizationOrThrow(Long id) {
        return organizationRepository.findById(id)
                .orElseThrow(() -> new OrganizationNotFoundException(id));
    }
}