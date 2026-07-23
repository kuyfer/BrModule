package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateSubsidiaryRequest;
import cires.bemodule.dtos.requests.PatchSubsidiaryRequest;
import cires.bemodule.dtos.views.SubsidiaryDTO;
import cires.bemodule.entities.Subsidiary;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface SubsidiaryMapper {

    @Mapping(source = "organization.name", target = "organizationName")
    SubsidiaryDTO toSubsidiaryDto(Subsidiary subsidiary);

    Subsidiary toSubsidiary(CreateSubsidiaryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "organizationId", target = "organization.id")
    void patchSubsidiaryFromRequest(PatchSubsidiaryRequest request, @MappingTarget Subsidiary subsidiary);
}