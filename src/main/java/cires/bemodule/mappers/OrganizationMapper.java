package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.entities.Organization;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = { SubsidiaryMapper.class })
public interface OrganizationMapper {

    @Mapping(target = "subsidiaryCount",
            expression = "java(organization.getSubsidiaries() != null ? organization.getSubsidiaries().size() : 0)")
    OrganizationDTO toOrganizationDto(Organization organization);

    Organization toOrganization(CreateOrganizationRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchOrganizationFromRequest(PatchOrganizationRequest request, @MappingTarget Organization organization);
}