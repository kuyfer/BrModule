package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.CreateSubsidiaryRequest;
import cires.bemodule.dtos.requests.PatchSubsidiaryRequest;
import cires.bemodule.dtos.views.SubsidiaryDTO;
import cires.bemodule.entities.Subsidiary;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface SubsidiaryMapper {

    SubsidiaryDTO toSubsidiaryDto(Subsidiary subsidiary);

    Subsidiary toSubsidiary(CreateSubsidiaryRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchSubsidiaryFromRequest(PatchSubsidiaryRequest request, @MappingTarget Subsidiary subsidiary);

}
