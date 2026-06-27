package cires.bemodule.mappers;

import cires.bemodule.dtos.requests.PatchTrainerRequest;
import cires.bemodule.dtos.requests.PatchUserRequest;
import cires.bemodule.dtos.views.TrainerDTO;
import cires.bemodule.dtos.requests.CreateTrainerRequest;
import cires.bemodule.entities.Trainer;
import cires.bemodule.entities.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDTO toTrainerDTO(Trainer trainer);

    Trainer toTrainer (CreateTrainerRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchTrainerFromRequest(PatchTrainerRequest request, @MappingTarget Trainer trainer);
}
