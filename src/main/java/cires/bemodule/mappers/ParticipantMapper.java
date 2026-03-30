package cires.bemodule.mappers;

import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.entities.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "registrationSource", target = "registrationSource")
    ParticipantDTO toParticipantDto(Participant participant);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "phone", target = "phone")
    @Mapping(source = "address", target = "address")
    @Mapping(source = "registrationSource", target = "registrationSource")
    Participant toParticipant(ParticipantDTO participantDTO);

}

