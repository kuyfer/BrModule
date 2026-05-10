package cires.bemodule.mappers;

import cires.bemodule.dtos.TrainerDTO;
import cires.bemodule.entities.Trainer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainerMapper {

    TrainerDTO toTrainerDTO(Trainer trainer);

    Trainer toTrainer(TrainerDTO trainerDTO);

}
