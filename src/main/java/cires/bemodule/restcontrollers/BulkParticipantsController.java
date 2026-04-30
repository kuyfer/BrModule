//package cires.bemodule.restcontrollers;
//
//import cires.bemodule.dtos2.CreateParticipantRequest;
//import cires.bemodule.dtos2.DeleteParticipantRequest;
//import cires.bemodule.dtos2.UpdateParticipantRequest;
//import cires.bemodule.entities.Participant;
//import cires.bemodule.enums.BulkActionType;
//import cires.bemodule.services.ParticipantService;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.EnumMap;
//import java.util.Optional;
//import java.util.function.Function;
//
//@RestController
//@RequestMapping("/api/bulk-participants")
//public class BulkParticipantsController {
//    private final ParticipantService participantService;
//
//    public BulkParticipantsController(ParticipantService participantService) {
//        this.participantService = participantService;
//    }
//
//    public void processCreate(CreateParticipantRequest request) {
//        Participant participant = toParticipant(request);  // convert DTO → entity
//        participantService.createParticipant(participant);
//    }
//
//    public void processUpdate(UpdateParticipantRequest request) {
//        Participant participant = toParticipant(request);
//        participantService.updateParticipant(participant);
//    }
//
//    public void processDelete(DeleteParticipantRequest request) {
//        Participant participant = toParticipant(request);
//        participantService.deleteParticipant(participant);
//    }
//
//    private Participant toParticipant(CreateParticipantRequest req) { /* mapping */ }
//    private Participant toParticipant(UpdateParticipantRequest req) { /* mapping */ }
//    private Participant toParticipant(DeleteParticipantRequest req) { /* mapping */ }
//}

