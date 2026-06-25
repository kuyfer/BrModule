package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateOrganizationRequest;
import cires.bemodule.dtos.requests.PatchOrganizationRequest;
import cires.bemodule.dtos.views.OrganizationDTO;
import cires.bemodule.dtos.SubsidiarySummaryDTO;
import cires.bemodule.entities.Organization;
import cires.bemodule.entities.Subsidiary;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrganizationMapper {

//    @Mapping(target = "subsidiaryCount", expression = "java(organization.getSubsidiaries() != null ? organization.getSubsidiaries().size() : 0)")
//    @Mapping(target = "subsidiaries", source = "subsidiaries", qualifiedByName = "toSummary")
    OrganizationDTO toDto(Organization organization);

    Organization toEntity(CreateOrganizationRequest request);

    void updateEntity(PatchOrganizationRequest request, @MappingTarget Organization entity);

    @Named("toSummary")
    default List<SubsidiarySummaryDTO> toSummary(List<Subsidiary> subsidiaries) {
        if (subsidiaries == null) return List.of();
        return subsidiaries.stream()
                .map(s -> SubsidiarySummaryDTO.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .build())
                .collect(Collectors.toList());
    }
}