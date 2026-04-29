package cires.bemodule.services;

import cires.bemodule.entities.Organization;
import cires.bemodule.entities.Subsidiary;
import cires.bemodule.repositories.OrganizationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

// TODO: fix this doesnt make any sense...
@Service
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    public OrganizationService(OrganizationRepository organizationRepository) {this.organizationRepository = organizationRepository;}

    // ################################# READ ########################################
    // ################################# CREATE ######################################
    // ################################# UPDATE ######################################
    // ################################# DELETE ######################################
    public void createOrganization(String name) {
        Organization organization = new Organization();
        organization.setName(name);
        organizationRepository.save(organization);
    }

    public Organization findOrganizationById(Long id){return organizationRepository.findById(id).orElseThrow();}

    public Optional<Organization> findOrganizationByName(String name){
        return organizationRepository.findByName(name);
    }

    public void addSubsidiary(Subsidiary subsidiary, Organization organization){
        organization.getSubsidiaries().add(subsidiary);
        organizationRepository.save(organization);
    }

    public void deleteSubsidiary(Subsidiary subsidiary, Organization organization){
        organization.getSubsidiaries().remove(subsidiary);
        organizationRepository.save(organization);
    }

    public Long countOrganizations() {
       return organizationRepository.count();
    }

}
