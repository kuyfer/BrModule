package cires.bemodule.mappers;

import cires.bemodule.dtos.ParticipantDTO;
import cires.bemodule.dtos2.CreateParticipantRequest;
import cires.bemodule.dtos2.CreateParticipantResponse;
import cires.bemodule.dtos2.PatchParticipantRequest;
import cires.bemodule.entities.Participant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {


    ParticipantDTO toParticipantDto(Participant participant);

    Participant toParticipant(ParticipantDTO participantDTO);

    @Mapping(source = "phoneNumber", target = "phone")
    Participant toParticipant(CreateParticipantRequest request);

    @Mapping(source = "phone", target = "phoneNumber")
    CreateParticipantResponse toCreateParticipantResponse(Participant participant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchParticipantFromRequest(PatchParticipantRequest request, @MappingTarget Participant participant);

}

