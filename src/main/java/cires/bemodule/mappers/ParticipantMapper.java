package cires.bemodule.mappers;

import cires.bemodule.dtos.views.ParticipantDTO;
import cires.bemodule.dtos.requests.CreateParticipantRequest;
import cires.bemodule.dtos.responses.CreateParticipantResponse;
import cires.bemodule.dtos.requests.PatchParticipantRequest;
import cires.bemodule.entities.Participant;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ParticipantMapper {

    ParticipantDTO toParticipantDto(Participant participant);

    Participant toParticipant(ParticipantDTO participantDTO);

    Participant toParticipant(CreateParticipantRequest request);

    CreateParticipantResponse toCreateParticipantResponse(Participant participant);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void patchParticipantFromRequest(PatchParticipantRequest request, @MappingTarget Participant participant);

}