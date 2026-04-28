package cires.bemodule.mappers;


import cires.bemodule.dtos.TrainingSessionDTO;
import cires.bemodule.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrainingSessionMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "trainer", target = "trainer")
   // @Mapping(source = "subsidiary", target = "subsidiary")
    @Mapping(source = "sessionParticipants", target = "sessionParticipants")
    TrainingSessionDTO toDto(TrainingSession trainingSession);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "title", target = "title")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "location", target = "location")
    @Mapping(source = "startDate", target = "startDate")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "mode", target = "mode")
    @Mapping(source = "trainer", target = "trainer")
    //@Mapping(source = "subsidiary", target = "subsidiary")
    @Mapping(source = "sessionParticipants", target = "sessionParticipants")
    TrainingSession toTrainingSession(TrainingSessionDTO trainingSessionDTO);

}
