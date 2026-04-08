package cires.bemodule.mappers;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.entities.Trainer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "specialty", target = "specialty")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "affiliatedOrganizations", target = "affiliatedOrganizations")
    TrainerDTO toTrainerDTO(Trainer trainer);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "specialty", target = "specialty")
    @Mapping(source = "user", target = "user")
    @Mapping(source = "affiliatedOrganizations", target = "affiliatedOrganizations")
    Trainer toTrainer(TrainerDTO trainerDTO);

}
