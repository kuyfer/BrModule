package cires.bemodule.mappers;

import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.*;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainingSessionMapper {

    TrainingSessionDTO toTrainingSessionDto(TrainingSession trainingSession);

    //@Mapping(ignore = true, target = "subsidiary")
    // TODO: add later when subsidiary is implemented to training session
    TrainingSession toTrainingSession(TrainingSessionDTO trainingSessionDTO);

}
