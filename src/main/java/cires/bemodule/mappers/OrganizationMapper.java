package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.entities.Organization;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = { SubsidiaryMapper.class })
public interface OrganizationMapper {

    @Mapping(target = "subsidiaryCount",
            expression = "java(organization.getSubsidiaries() != null ? organization.getSubsidiaries().size() : 0)")
    @Mapping(target = "subsidiaries", source = "subsidiaries")
    OrganizationDTO toDto(Organization organization);

    Organization toEntity(CreateOrganizationRequest request);

    void updateEntity(PatchOrganizationRequest request, @MappingTarget Organization entity);
}